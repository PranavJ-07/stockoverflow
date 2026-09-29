import React from 'react';
import { Product } from '../types';
import { StatusBadge } from './Badges';
import { UpdateStockModal } from './UpdateStockModal';
import { CreateOrderModal } from './CreateOrderModal';

interface ProductTableProps {
  products: Product[];
  loading: boolean;
  error: string | null;
  onRefresh: () => void;
}

export const ProductTable: React.FC<ProductTableProps> = ({ 
  products, 
  loading, 
  error, 
  onRefresh 
}) => {
  const [showUpdateStockModal, setShowUpdateStockModal] = React.useState(false);
  const [showCreateOrderModal, setShowCreateOrderModal] = React.useState(false);
  const [selectedProduct, setSelectedProduct] = React.useState<Product | null>(null);

  const handleUpdateStock = (product: Product) => {
    setSelectedProduct(product);
    setShowUpdateStockModal(true);
  };

  const handleCreateOrder = (product: Product) => {
    setSelectedProduct(product);
    setShowCreateOrderModal(true);
  };

  const handleSuggestPricing = (product: Product) => {
    // TODO: Implement pricing suggestion
    alert(`Pricing suggestion for ${product.name} would be generated here`);
  };

  const handleSuggestReorder = (product: Product) => {
    // TODO: Implement reorder suggestion
    alert(`Reorder suggestion for ${product.name} would be generated here`);
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-6 text-center">
        <div className="text-red-600">Error: {error}</div>
        <button
          onClick={onRefresh}
          className="mt-4 inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
        >
          Try Again
        </button>
      </div>
    );
  }

  if (products.length === 0) {
    return (
      <div className="p-6 text-center">
        <div className="text-gray-500">No products found</div>
      </div>
    );
  }

  const isLowStock = (product: Product) => 
    product.stockLevel < product.reorderThreshold && product.stockLevel > 0;

  return (
    <>
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                SKU
              </th>
              <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Product
              </th>
              <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Category
              </th>
              <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Price
              </th>
              <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Stock
              </th>
              <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Threshold
              </th>
              <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
          </tbody>
        </table>
      </div>
      
      {selectedProduct && (
        <>
          <UpdateStockModal
            isOpen={showUpdateStockModal}
            onClose={() => setShowUpdateStockModal(false)}
            product={selectedProduct}
            onRefresh={onRefresh}
          />
          <CreateOrderModal
            isOpen={showCreateOrderModal}
            onClose={() => setShowCreateOrderModal(false)}
            product={selectedProduct}
            onRefresh={onRefresh}
          />
        </>
      )}
    </>
  );
};
                Demand
              </th>
              <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Status
              </th>
              <th scope="col" className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                Actions
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">