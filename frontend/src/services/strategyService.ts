import { apiClient } from './apiClient';
import { Strategy, StrategyUpdateRequest } from '../types';

export const strategyService = {
  getCurrentStrategy: () => apiClient.getStrategy(),
  
  updateStrategy: (request: StrategyUpdateRequest) => 
    apiClient.updateStrategy(request.pricingStrategy, request.reorderStrategy),
};