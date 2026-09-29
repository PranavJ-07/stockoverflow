import React from 'react';
import { Link, useLocation } from 'react-router-dom';

export const Header: React.FC = () => {
  const location = useLocation();
  
  return (
    <header className="bg-white shadow">
      <div className="max-w-7xl mx-auto px-4 py-6 sm:px-6 lg:px-8 flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-900">StockPulse</h1>
        <nav className="flex space-x-8">
          <Link 
            to="/" 
            className={`px-3 py-2 rounded-md text-sm font-medium ${
              location.pathname === '/' 
                ? 'bg-blue-100 text-blue-700' 
                : 'text-gray-500 hover:text-gray-700'
            }`}
          >
            Overview
          </Link>
          <Link 
            to="/products" 
            className={`px-3 py-2 rounded-md text-sm font-medium ${
              location.pathname === '/products' 
                ? 'bg-blue-100 text-blue-700' 
                : 'text-gray-500 hover:text-gray-700'
            }`}
          >
            Products
          </Link>
          <Link 
            to="/suggestions" 
            className={`px-3 py-2 rounded-md text-sm font-medium ${
              location.pathname === '/suggestions' 
                ? 'bg-blue-100 text-blue-700' 
                : 'text-gray-500 hover:text-gray-700'
            }`}
          >
            Suggestions
          </Link>
        </nav>
      </div>
    </header>
  );
};