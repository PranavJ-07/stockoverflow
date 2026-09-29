import { apiClient } from './apiClient';
import { SuggestionStatus } from '../types';

export const suggestionService = {
  updatePricingSuggestionStatus: (suggestionId: number, status: SuggestionStatus) => 
    apiClient.updatePricingSuggestionStatus(suggestionId, status),
  
  updateReorderSuggestionStatus: (suggestionId: number, status: SuggestionStatus) => 
    apiClient.updateReorderSuggestionStatus(suggestionId, status),
};