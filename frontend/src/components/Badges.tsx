import React from 'react';
import { ProductStatus, SuggestionStatus, TriggerReason } from '../types';

interface StatusBadgeProps {
  status: ProductStatus | SuggestionStatus;
}

export const StatusBadge: React.FC<StatusBadgeProps> = ({ status }) => {
  const getStatusClass = () => {
    switch (status) {
      case 'ACTIVE':
        return 'bg-green-100 text-green-800';
      case 'OUT_OF_STOCK':
        return 'bg-red-100 text-red-800';
      case 'PRICE_REVIEW_PENDING':
        return 'bg-yellow-100 text-yellow-800';
      case 'PENDING':
        return 'bg-yellow-100 text-yellow-800';
      case 'ACCEPTED':
        return 'bg-green-100 text-green-800';
      case 'REJECTED':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusClass()}`}>
      {status.replace('_', ' ')}
    </span>
  );
};

interface TriggerBadgeProps {
  trigger: TriggerReason;
}

export const TriggerBadge: React.FC<TriggerBadgeProps> = ({ trigger }) => {
  const getTriggerClass = () => {
    switch (trigger) {
      case 'INVENTORY_LOW':
        return 'bg-orange-100 text-orange-800';
      case 'DEMAND_SPIKE':
        return 'bg-purple-100 text-purple-800';
      case 'MANUAL':
        return 'bg-blue-100 text-blue-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getTriggerClass()}`}>
      {trigger.replace('_', ' ')}
    </span>
  );
};