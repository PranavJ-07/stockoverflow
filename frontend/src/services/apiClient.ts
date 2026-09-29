const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

export const apiClient = {
  // Products
  getProducts: async (status?: string, category?: string) => {
    const params = new URLSearchParams();
    if (status) params.append('status', status);
    if (category) params.append('category', category);
    
    const response = await fetch(`${API_BASE_URL}/api/products?${params.toString()}`);
    if (!response.ok) {
      throw new Error(`Failed to fetch products: ${response.status}`);
    }
    return response.json();
  },

  getProductById: async (id: number) => {
    const response = await fetch(`${API_BASE_URL}/api/products/${id}`);
    if (!response.ok) {
      throw new Error(`Failed to fetch product: ${response.status}`);
    }
    return response.json();
  },

  updateStock: async (id: number, stockLevel: number) => {
    const response = await fetch(`${API_BASE_URL}/api/products/${id}/stock`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ stockLevel }),
    });
    if (!response.ok) {
      throw new Error(`Failed to update stock: ${response.status}`);
    }
    return response.json();
  },

  simulateSale: async (id: number, quantity: number) => {
    const response = await fetch(`${API_BASE_URL}/api/products/${id}/orders`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ quantity }),
    });
    if (!response.ok) {
      throw new Error(`Failed to simulate sale: ${response.status}`);
    }
    return response.json();
  },

  // Suggestions
  createPricingSuggestion: async (productId: number) => {
    const response = await fetch(`${API_BASE_URL}/api/products/${productId}/suggest-pricing`, {
      method: 'POST',
    });
    if (!response.ok) {
      throw new Error(`Failed to create pricing suggestion: ${response.status}`);
    }
    return response.json();
  },

  createReorderSuggestion: async (productId: number) => {
    const response = await fetch(`${API_BASE_URL}/api/products/${productId}/suggest-reorder`, {
      method: 'POST',
    });
    if (!response.ok) {
      throw new Error(`Failed to create reorder suggestion: ${response.status}`);
    }
    return response.json();
  },

  updatePricingSuggestionStatus: async (suggestionId: number, status: string) => {
    const response = await fetch(`${API_BASE_URL}/api/pricing-suggestions/${suggestionId}`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ status }),
    });
    if (!response.ok) {
      throw new Error(`Failed to update pricing suggestion status: ${response.status}`);
    }
    return response.json();
  },

  updateReorderSuggestionStatus: async (suggestionId: number, status: string) => {
    const response = await fetch(`${API_BASE_URL}/api/reorder-suggestions/${suggestionId}`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ status }),
    });
    if (!response.ok) {
      throw new Error(`Failed to update reorder suggestion status: ${response.status}`);
    }
    return response.json();
  },

  // Strategy
  getStrategy: async () => {
    const response = await fetch(`${API_BASE_URL}/api/admin/strategy`);
    if (!response.ok) {
      throw new Error(`Failed to fetch strategy: ${response.status}`);
    }
    return response.json();
  },

  updateStrategy: async (pricingStrategy: string, reorderStrategy: string) => {
    const response = await fetch(`${API_BASE_URL}/api/admin/strategy`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ pricingStrategy, reorderStrategy }),
    });
    if (!response.ok) {
      throw new Error(`Failed to update strategy: ${response.status}`);
    }
    return response.json();
  },
};