import React, { useState } from 'react';
import { useProducts } from '../hooks/useProducts';
import { ProductTable } from '../components/ProductTable';
import { ProductStatus, Category } from '../types';

export const ProductsPage: React.FC = () => {
  const [statusFilter, setStatusFilter] = useState<ProductStatus | ''>('');
  const [categoryFilter, setCategoryFilter] = useState<Category | ''>('');

  const { products, loading, error, refresh } = useProducts(
    statusFilter || undefined,
    categoryFilter || undefined
  );

  const handleRefresh = () => {
    refresh();
  };

  return (
    <div>
      <div className="md:flex md:items-center md:justify-between mb-6">
        <div className="flex-1 min-w-0">
          <h2 className="text-2xl font-bold leading-7 text-gray-900 sm:text-3xl sm:truncate">
            Products
          </h2>
        </div>
        <div className="mt-4 flex md:mt-0 md:ml-4">
          <button
            type="button"
            onClick={handleRefresh}
            className="ml-3 inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
          >
            Refresh
          </button>
        </div>
      </div>

      {/* Filters */}
      <div className="mb-6 grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4">
        <div>
          <label htmlFor="status-filter" className="block text-sm font-medium text-gray-700">
            Status
          </label>
          <select
            id="status-filter"
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value as ProductStatus || '')}
            className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm rounded-md"
          >
            <option value="">All Statuses</option>
            <option value="ACTIVE">Active</option>
            <option value="PRICE_REVIEW_PENDING">Price Review Pending</option>
            <option value="OUT_OF_STOCK">Out of Stock</option>
          </select>
        </div>

        <div>
          <label htmlFor="category-filter" className="block text-sm font-medium text-gray-700">
            Category
          </label>
          <select
            id="category-filter"
            value={categoryFilter}
            onChange={(e) => setCategoryFilter(e.target.value as Category || '')}
            className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm rounded-md"
          >
            <option value="">All Categories</option>
            <option value="ELECTRONICS">Electronics</option>
            <option value="APPAREL">Apparel</option>
            <option value="HOME">Home</option>
          </select>
        </div>
      </div>

      {/* Product Table */}
      <div className="bg-white shadow overflow-hidden sm:rounded-lg">
        <div className="px-4 py-5 sm:px-6">
          <h3 className="text-lg leading-6 font-medium text-gray-900">
            Inventory Overview
          </h3>
        </div>
        <div className="border-t border-gray-200">
          <ProductTable 
            products={products || []} 
            loading={loading} 
            error={error} 
            onRefresh={handleRefresh}
          />
        </div>
      </div>
    </div>
  );
};