import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GameState, PlayerResponse } from '../models/game-state.model';

@Injectable({ providedIn: 'root' })
export class GameService {
  private readonly base = '/api';

  constructor(private http: HttpClient) {}

  createPlayer(name: string): Observable<PlayerResponse> {
    return this.http.post<PlayerResponse>(`${this.base}/players`, { name });
  }

  getGameState(playerId: string): Observable<GameState> {
    return this.http.get<GameState>(`${this.base}/game/${playerId}`);
  }

  collectCoins(playerId: string): Observable<GameState> {
    return this.http.post<GameState>(`${this.base}/game/${playerId}/collect`, {});
  }

  buyGenerator(playerId: string, generatorType: string): Observable<GameState> {
    return this.http.post<GameState>(`${this.base}/game/${playerId}/buy/generator`, { generatorType });
  }

  buyUpgrade(playerId: string, upgradeType: string): Observable<GameState> {
    return this.http.post<GameState>(`${this.base}/game/${playerId}/buy/upgrade`, { upgradeType });
  }
}
