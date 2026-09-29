import { useState, useEffect } from 'react';
import { productService } from '../services/productService';
import { Product, ProductStatus, Category } from '../types';

export const useProducts = (status?: ProductStatus, category?: Category) => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        setLoading(true);
        const data = await productService.getAll(status, category);
        setProducts(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to fetch products');
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, [status, category]);

  return { products, loading, error, refresh: () => {
    setLoading(true);
    setError(null);
    productService.getAll(status, category)
      .then(setProducts)
      .catch(err => setError(err instanceof Error ? err.message : 'Failed to fetch products'))
      .finally(() => setLoading(false));
  }};
};