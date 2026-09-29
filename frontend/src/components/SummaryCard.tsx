import React from 'react';

interface SummaryCardProps {
  title: string;
  value: string | number;
  description?: string;
  color?: string;
}

export const SummaryCard: React.FC<SummaryCardProps> = ({ 
  title, 
  value, 
  description,
  color = 'blue'
}) => {
  const colorClasses = {
    blue: 'bg-blue-50 text-blue-800 border-blue-200',
    green: 'bg-green-50 text-green-800 border-green-200',
    yellow: 'bg-yellow-50 text-yellow-800 border-yellow-200',
    red: 'bg-red-50 text-red-800 border-red-200',
    purple: 'bg-purple-50 text-purple-800 border-purple-200',
  };

  return (
    <div className={`border rounded-lg p-6 shadow-sm ${colorClasses[color as keyof typeof colorClasses] || colorClasses.blue}`}>
      <h3 className="text-lg font-medium">{title}</h3>
      <p className="mt-2 text-3xl font-semibold">{value}</p>
      {description && <p className="mt-1 text-sm">{description}</p>}
    </div>
  );
};