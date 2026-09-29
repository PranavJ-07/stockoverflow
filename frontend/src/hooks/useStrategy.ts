import { useState, useEffect } from 'react';
import { strategyService } from '../services/strategyService';
import { Strategy } from '../types';

export const useStrategy = () => {
  const [strategy, setStrategy] = useState<Strategy | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchStrategy = async () => {
      try {
        setLoading(true);
        const data = await strategyService.getCurrentStrategy();
        setStrategy(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to fetch strategy');
      } finally {
        setLoading(false);
      }
    };

    fetchStrategy();
  }, []);

  const updateStrategy = async (pricingStrategy: string, reorderStrategy: string) => {
    try {
      const updatedStrategy = await strategyService.updateStrategy({ pricingStrategy, reorderStrategy });
      setStrategy(updatedStrategy);
      return { success: true, data: updatedStrategy };
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Failed to update strategy';
      setError(errorMessage);
      return { success: false, error: errorMessage };
    }
  };

  return { strategy, loading, error, updateStrategy };
};