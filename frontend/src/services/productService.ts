import { apiClient } from './apiClient';
import { Product, ProductStatus, Category } from '../types';

export const productService = {
  getAll: (status?: ProductStatus, category?: Category) => 
    apiClient.getProducts(status, category),
  
  getById: (id: number) => 
    apiClient.getProductById(id),
  
  updateStock: (id: number, stockLevel: number) => 
    apiClient.updateStock(id, stockLevel),
  
  simulateSale: (id: number, quantity: number) => 
    apiClient.simulateSale(id, quantity),
  
  createPricingSuggestion: (productId: number) => 
    apiClient.createPricingSuggestion(productId),
  
  createReorderSuggestion: (productId: number) => 
    apiClient.createReorderSuggestion(productId),
};