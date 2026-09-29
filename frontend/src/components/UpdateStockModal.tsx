import React, { useState } from 'react';
import { Product } from '../types';
import { productService } from '../services/productService';

interface UpdateStockModalProps {
  isOpen: boolean;
  onClose: () => void;
  product: Product;
  onRefresh: () => void;
}

export const UpdateStockModal: React.FC<UpdateStockModalProps> = ({ 
  isOpen, 
  onClose, 
  product,
  onRefresh
}) => {
  const [stockLevel, setStockLevel] = useState(product.stockLevel.toString());
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    
    try {
      const newStockLevel = parseInt(stockLevel);
      if (isNaN(newStockLevel) || newStockLevel < 0) {
        throw new Error('Please enter a valid positive number');
      }
      
      await productService.updateStock(product.id, newStockLevel);
      onRefresh();
      onClose();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update stock');
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div className="mt-3">
          <h3 className="text-lg leading-6 font-medium text-gray-900 mb-4">
            Update Stock Level
          </h3>
          <div className="mb-4">
            <p className="text-sm text-gray-500">
              <strong>Product:</strong> {product.name} ({product.sku})
            </p>
            <p className="text-sm text-gray-500">
              <strong>Current Stock:</strong> {product.stockLevel}
            </p>
          </div>
          
          <form onSubmit={handleSubmit}>
            <div className="mb-4">
              <label htmlFor="stockLevel" className="block text-sm font-medium text-gray-700">
                New Stock Level
              </label>
              <input
                type="number"
                id="stockLevel"
                value={stockLevel}
                onChange={(e) => setStockLevel(e.target.value)}
                min="0"
                className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                required
              />
            </div>
            
            {error && (
              <div className="mb-4 text-sm text-red-600">
                {error}
              </div>
            )}
            
            <div className="flex justify-end space-x-3">
              <button
                type="button"
                onClick={onClose}
                disabled={loading}
                className="inline-flex justify-center px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
              >
                Cancel
              </button>
              <button
                type="submit"
                disabled={loading}
                className="inline-flex justify-center px-4 py-2 text-sm font-medium text-white bg-blue-600 border border-transparent rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50"
              >
                {loading ? 'Updating...' : 'Update Stock'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};