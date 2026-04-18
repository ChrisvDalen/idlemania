export interface GeneratorState {
  type: string;
  displayName: string;
  count: number;
  nextCost: number;
  incomePerSecond: number;
}

export interface UpgradeState {
  type: string;
  displayName: string;
  cost: number;
  purchased: boolean;
  targetGenerator: string;
  multiplier: number;
}

export interface GameState {
  playerId: string;
  playerName: string;
  coins: number;
  totalCoinsEarned: number;
  incomePerSecond: number;
  generators: GeneratorState[];
  upgrades: UpgradeState[];
}

export interface PlayerResponse {
  id: string;
  name: string;
}
