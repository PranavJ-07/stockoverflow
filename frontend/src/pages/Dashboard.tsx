import React, { useState, useEffect } from 'react';
import { SummaryCard } from '../components/SummaryCard';
import { useProducts } from '../hooks/useProducts';
import { useStrategy } from '../hooks/useStrategy';
import { StrategySelector } from '../components/StrategySelector';
import { Product, ProductStatus, SuggestionStatus } from '../types';

export const DashboardPage: React.FC = () => {
  const { products, loading: productsLoading } = useProducts();
  const { strategy, loading: strategyLoading } = useStrategy();
  
  const [lowStockCount, setLowStockCount] = useState(0);
  const [outOfStockCount, setOutOfStockCount] = useState(0);
  const [pendingSuggestionsCount, setPendingSuggestionsCount] = useState(0);
  const [demandSpikesCount, setDemandSpikesCount] = useState(0);

  useEffect(() => {
    if (products && products.length > 0) {
      // Calculate summary metrics
      const lowStock = products.filter(p => 
        p.stockLevel < p.reorderThreshold && p.stockLevel > 0
      ).length;
      
      const outOfStock = products.filter(p => 
        p.status === ProductStatus.OUT_OF_STOCK
      ).length;
      
      setLowStockCount(lowStock);
      setOutOfStockCount(outOfStock);
      setPendingSuggestionsCount(0); // We'll need to fetch suggestions separately
      setDemandSpikesCount(0); // We'll need to implement demand spike detection
    }
  }, [products]);

  if (productsLoading || strategyLoading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  return (
    <div>
      <div className="md:flex md:items-center md:justify-between mb-6">
        <div className="flex-1 min-w-0">
          <h2 className="text-2xl font-bold leading-7 text-gray-900 sm:text-3xl sm:truncate">
            AI Inventory & Pricing Console
          </h2>
        </div>
      </div>

      {/* Strategy Selector */}
      <div className="mb-8">
        <StrategySelector strategy={strategy} />
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4 mb-8">
        <SummaryCard 
          title="Total Products" 
          value={products?.length || 0} 
          color="blue"
        />
        <SummaryCard 
          title="Low Stock" 
          value={lowStockCount} 
          description="Below reorder threshold"
          color="yellow"
        />
        <SummaryCard 
          title="Out of Stock" 
          value={outOfStockCount} 
          color="red"
        />
        <SummaryCard 
          title="Pending Suggestions" 
          value={pendingSuggestionsCount} 
          color="purple"
        />
      </div>

      {/* Recent Activity / Agent Activity */}
      <div className="bg-white shadow overflow-hidden sm:rounded-lg">
        <div className="px-4 py-5 sm:px-6">
          <h3 className="text-lg leading-6 font-medium text-gray-900">
            Recent Activity
          </h3>
          <p className="mt-1 max-w-2xl text-sm text-gray-500">
            Latest inventory and pricing activities
          </p>
        </div>
        <div className="border-t border-gray-200">
          <div className="p-6">
            <p className="text-gray-500 italic">Activity feed will be implemented in a future update</p>
          </div>
        </div>
      </div>
    </div>
  );
};