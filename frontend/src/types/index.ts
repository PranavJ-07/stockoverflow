export interface Product {
  id: number;
  sku: string;
  name: string;
  category: Category;
  currentPrice: number;
  stockLevel: number;
  reorderThreshold: number;
  demandVelocity: number;
  status: ProductStatus;
  createdAt: string;
  updatedAt: string;
}

export interface PricingSuggestion {
  id: number;
  product: Product;
  currentPrice: number;
  recommendedPrice: number;
  direction: Direction;
  confidence: number;
  reasoning: string;
  status: SuggestionStatus;
  triggerReason: TriggerReason;
  createdAt: string;
}

export interface ReorderSuggestion {
  id: number;
  product: Product;
  currentStock: number;
  recommendedQuantity: number;
  suggestedLeadTimeDays: number;
  confidence: number;
  reasoning: string;
  status: SuggestionStatus;
  triggerReason: TriggerReason;
  createdAt: string;
}

export interface Strategy {
  pricingStrategy: PricingStrategyType;
  reorderStrategy: ReorderStrategyType;
}

export interface StrategyUpdateRequest {
  pricingStrategy: PricingStrategyType;
  reorderStrategy: ReorderStrategyType;
}

export enum Category {
  ELECTRONICS = 'ELECTRONICS',
  APPAREL = 'APPAREL',
  HOME = 'HOME'
}

export enum ProductStatus {
  ACTIVE = 'ACTIVE',
  PRICE_REVIEW_PENDING = 'PRICE_REVIEW_PENDING',
  OUT_OF_STOCK = 'OUT_OF_STOCK'
}

export enum Direction {
  INCREASE = 'INCREASE',
  DECREASE = 'DECREASE',
  HOLD = 'HOLD'
}

export enum SuggestionStatus {
  PENDING = 'PENDING',
  ACCEPTED = 'ACCEPTED',
  REJECTED = 'REJECTED'
}

export enum TriggerReason {
  INITIAL = 'INITIAL',
  INVENTORY_LOW = 'INVENTORY_LOW',
  DEMAND_SPIKE = 'DEMAND_SPIKE',
  MANUAL = 'MANUAL'
}

export enum PricingStrategyType {
  RULE_BASED = 'RULE_BASED',
  AI = 'AI'
}

export enum ReorderStrategyType {
  RULE_BASED = 'RULE_BASED',
  AI = 'AI'
}

export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
}

export interface ApiError {
  timestamp: string;
  status: number;
  message: string;
  path: string;
}