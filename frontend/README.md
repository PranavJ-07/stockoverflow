# StockPulse React Dashboard

This is the React dashboard frontend for the StockPulse AI Inventory & Dynamic Pricing Engine.

## Tech Stack

- React 18
- TypeScript
- Vite
- Tailwind CSS

## Prerequisites

- Node.js (v16 or higher)
- npm or yarn

## Setup

1. Install dependencies:
```bash
npm install
```

2. Create a `.env` file based on `.env.example`:
```bash
cp .env.example .env
```

3. Start the development server:
```bash
npm run dev
```

The dashboard will be available at http://localhost:5173

## Environment Variables

- `VITE_API_BASE_URL`: The base URL for the backend API (default: http://localhost:8080)

## Build for Production

```bash
npm run build
```

The built files will be in the `dist` directory.

## Features

- Product inventory management
- Stock level monitoring
- Demand velocity tracking
- Strategy selection (Rule-based vs AI)
- Responsive design