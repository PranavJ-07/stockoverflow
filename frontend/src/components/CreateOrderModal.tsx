import React, { useState } from 'react';
import { Product } from '../types';
import { productService } from '../services/productService';

interface CreateOrderModalProps {
  isOpen: boolean;
  onClose: () => void;
  product: Product;
  onRefresh: () => void;
}

export const CreateOrderModal: React.FC<CreateOrderModalProps> = ({ 
  isOpen, 
  onClose, 
  product,
  onRefresh
}) => {
  const [quantity, setQuantity] = useState('1');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    
    try {
      const orderQuantity = parseInt(quantity);
      if (isNaN(orderQuantity) || orderQuantity <= 0) {
        throw new Error('Please enter a valid positive number');
      }
      
      if (orderQuantity > product.stockLevel) {
        throw new Error(`Quantity cannot exceed available stock (${product.stockLevel})`);
      }
      
      await productService.simulateSale(product.id, orderQuantity);
      onRefresh();
      onClose();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create order');
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
            Create Order
          </h3>
          <div className="mb-4">
            <p className="text-sm text-gray-500">
              <strong>Product:</strong> {product.name} ({product.sku})
            </p>
            <p className="text-sm text-gray-500">
              <strong>Available Stock:</strong> {product.stockLevel}
            </p>
          </div>
          
          <form onSubmit={handleSubmit}>
            <div className="mb-4">
              <label htmlFor="quantity" className="block text-sm font-medium text-gray-700">
                Quantity
              </label>
              <input
                type="number"
                id="quantity"
                value={quantity}
                onChange={(e) => setQuantity(e.target.value)}
                min="1"
                max={product.stockLevel}
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
                className="inline-flex justify-center px-4 py-2 text-sm font-medium text-white bg-green-600 border border-transparent rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 disabled:opacity-50"
              >
                {loading ? 'Processing...' : 'Create Order'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};