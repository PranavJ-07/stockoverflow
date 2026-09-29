import React, { useState } from 'react';
import { Strategy, PricingStrategyType, ReorderStrategyType } from '../types';
import { useStrategy } from '../hooks/useStrategy';

interface StrategySelectorProps {
  strategy: Strategy | null;
}

export const StrategySelector: React.FC<StrategySelectorProps> = ({ strategy }) => {
  const { updateStrategy } = useStrategy();
  const [pricingStrategy, setPricingStrategy] = useState(strategy?.pricingStrategy || PricingStrategyType.RULE_BASED);
  const [reorderStrategy, setReorderStrategy] = useState(strategy?.reorderStrategy || ReorderStrategyType.RULE_BASED);

  const handleUpdateStrategy = async () => {
    const result = await updateStrategy(pricingStrategy, reorderStrategy);
    if (result.success) {
      alert('Strategy updated successfully!');
    } else {
      alert(`Failed to update strategy: ${result.error}`);
    }
  };

  return (
    <div className="bg-white shadow rounded-lg p-6">
      <h3 className="text-lg font-medium text-gray-900 mb-4">Commerce Strategy</h3>
      
      <div className="grid grid-cols-1 gap-6 sm:grid-cols-2">
        <div>
          <label htmlFor="pricing-strategy" className="block text-sm font-medium text-gray-700 mb-1">
            Pricing Strategy
          </label>
          <select
            id="pricing-strategy"
            value={pricingStrategy}
            onChange={(e) => setPricingStrategy(e.target.value as PricingStrategyType)}
            className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm rounded-md"
          >
            <option value={PricingStrategyType.RULE_BASED}>Rule Based</option>
            <option value={PricingStrategyType.AI}>AI</option>
          </select>
        </div>

        <div>
          <label htmlFor="reorder-strategy" className="block text-sm font-medium text-gray-700 mb-1">
            Reorder Strategy
          </label>
          <select
            id="reorder-strategy"
            value={reorderStrategy}
            onChange={(e) => setReorderStrategy(e.target.value as ReorderStrategyType)}
            className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm rounded-md"
          >
            <option value={ReorderStrategyType.RULE_BASED}>Rule Based</option>
            <option value={ReorderStrategyType.AI}>AI</option>
          </select>
        </div>
      </div>

      <div className="mt-6">
        <button
          onClick={handleUpdateStrategy}
          className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
        >
          Update Strategy
        </button>
        
        <div className="mt-4 text-sm text-gray-500">
          <p><strong>Current:</strong> Pricing: {strategy?.pricingStrategy || 'Loading...'}, Reorder: {strategy?.reorderStrategy || 'Loading...'}</p>
        </div>
      </div>
    </div>
  );
};