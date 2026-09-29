import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Header } from './components/Header';
import { DashboardPage } from './pages/Dashboard';
import { ProductsPage } from './pages/Products';
import { SuggestionsPage } from './pages/Suggestions';

export const App: React.FC = () => {
  return (
    <Router>
      <div className="min-h-full">
        <Header />
        <main>
          <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
            <Routes>
              <Route path="/" element={<DashboardPage />} />
              <Route path="/products" element={<ProductsPage />} />
              <Route path="/suggestions" element={<SuggestionsPage />} />
            </Routes>
          </div>
        </main>
      </div>
    </Router>
  );
};