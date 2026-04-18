import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { interval, Subscription } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { GameService } from './services/game.service';
import { GameState } from './models/game-state.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  playerName = '';
  playerId: string | null = null;
  gameState: GameState | null = null;
  error: string | null = null;
  private pollSub?: Subscription;

  constructor(private gameService: GameService) {}

  ngOnInit(): void {
    const saved = localStorage.getItem('playerId');
    if (saved) {
      this.playerId = saved;
      this.loadState();
      this.startPolling();
    }
  }

  ngOnDestroy(): void {
    this.pollSub?.unsubscribe();
  }

  createPlayer(): void {
    if (!this.playerName.trim()) return;
    this.gameService.createPlayer(this.playerName.trim()).subscribe({
      next: player => {
        this.playerId = player.id;
        localStorage.setItem('playerId', player.id);
        this.loadState();
        this.startPolling();
      },
      error: () => { this.error = 'Failed to create player. Is the backend running?'; }
    });
  }

  collectCoins(): void {
    this.gameService.collectCoins(this.playerId!).subscribe({
      next: state => { this.gameState = state; },
      error: () => { this.error = 'Action failed.'; }
    });
  }

  buyGenerator(type: string): void {
    this.gameService.buyGenerator(this.playerId!, type).subscribe({
      next: state => { this.gameState = state; },
      error: err => { this.error = err.error?.message || 'Not enough coins.'; }
    });
  }

  buyUpgrade(type: string): void {
    this.gameService.buyUpgrade(this.playerId!, type).subscribe({
      next: state => { this.gameState = state; },
      error: err => { this.error = err.error?.message || 'Cannot purchase upgrade.'; }
    });
  }

  resetPlayer(): void {
    localStorage.removeItem('playerId');
    this.playerId = null;
    this.gameState = null;
    this.playerName = '';
    this.pollSub?.unsubscribe();
  }

  private loadState(): void {
    this.gameService.getGameState(this.playerId!).subscribe({
      next: state => { this.gameState = state; },
      error: () => { this.error = 'Could not load game state.'; }
    });
  }

  private startPolling(): void {
    this.pollSub = interval(5000).pipe(
      switchMap(() => this.gameService.getGameState(this.playerId!))
    ).subscribe({ next: state => { this.gameState = state; } });
  }
}
