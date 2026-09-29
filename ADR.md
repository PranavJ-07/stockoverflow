# StockPulse Architecture Decision Record

## 1. Context

StockPulse is an inventory-aware commerce advisor that helps merchandisers make informed pricing and reorder decisions. The system monitors inventory levels and demand patterns to automatically generate suggestions powered by either deterministic rules or AI algorithms. These suggestions go through a human approval checkpoint before any changes are made to product prices or inventory levels. The agentic loop continuously observes inventory changes, reasons about optimal actions, acts by generating suggestions, and checkpoints by requiring human approval.

## 2. Decision

### 2.1 Strategy Pattern

We implemented the Strategy Pattern to allow runtime switching between different pricing and reorder algorithms without requiring application restarts. This pattern separates the algorithm implementation from the client code that uses it. Two concrete strategies are implemented: RuleBasedPricingStrategy/RuleBasedReorderStrategy for deterministic logic and placeholder AiPricingStrategy/AiReorderStrategy for future AI integration. The CommerceAdvisor serves as the context that delegates to the currently active strategy.

### 2.2 AI Gateway

Communication with AI models is isolated behind an LLMGateway abstraction to decouple the business logic from specific AI provider implementations. This allows us to switch between different LLM providers or models without changing the core business logic. The gateway handles authentication, request formatting, and response parsing while exposing a clean interface to the strategy implementations.

### 2.3 AI Failure Handling

Our AI integration follows a fallback pattern where AI-generated recommendations are validated against business rules. If the AI fails or produces an invalid recommendation, the system gracefully falls back to the rule-based strategy. This ensures that the application continues to function even when AI services are unavailable, maintaining reliability and preventing single points of failure.

### 2.4 Agentic Loop

The agentic loop follows the OBSERVE → REASON → ACT → CHECKPOINT cycle. Inventory changes trigger events that are processed asynchronously to generate suggestions. The REASON phase uses the active strategy to determine optimal actions, while the CHECKPOINT phase requires human approval before any changes are applied to products. This ensures intelligent automation while maintaining human oversight.

### 2.5 Async Processing

Inventory-triggered AI processing is implemented asynchronously to prevent blocking HTTP requests. Using Spring's @Async annotation with a dedicated thread pool, the agentic loop processes inventory events without delaying immediate stock updates or order completions. This improves responsiveness and prevents cascade failures when external AI services are slow or unavailable.

### 2.6 Idempotency

To prevent duplicate suggestions, we implement idempotency at the suggestion creation level using a composite key: product + trigger + suggestion type + PENDING status. Before creating a new suggestion, the system checks for existing PENDING suggestions with the same characteristics and skips creation if one exists. This prevents spamming merchandisers with redundant recommendations.

### 2.7 SSE

Server-Sent Events (SSE) are used for real-time dashboard updates instead of polling or WebSockets because they provide a simpler, HTTP-based solution for one-way server-to-client communication. SSE integrates naturally with Spring's SseEmitter and provides efficient streaming of suggestion events to connected dashboards without the overhead of bidirectional communication.

### 2.8 Frontend/Backend Separation

The React frontend is completely separate from the Spring Boot backend, communicating only through REST APIs and SSE. This separation allows independent development, deployment, and scaling of frontend and backend components. The backend owns all domain logic and data persistence, while the frontend focuses on user experience and visualization.

## 3. Alternatives Considered

### Strategy Pattern

Alternatives:
* Hardcoded if/else logic scattered throughout the codebase
* Single monolithic strategy class with conditional behavior
* Strategy Pattern

Decision: Strategy Pattern
Tradeoffs: More classes and interfaces but much better extensibility, testability, and runtime configurability. Makes adding new AI strategies straightforward without touching existing code.

### Communication Mechanisms

Alternatives:
* REST API polling
* WebSocket bidirectional communication
* Server-Sent Events (SSE)

Decision: SSE
Tradeoffs: SSE provides simpler implementation for our use case (server-to-client notifications only) with lower overhead than WebSockets and better real-time behavior than polling.

### Failure Handling

Alternatives:
* Let AI failures crash the suggestion process
* Return error responses to clients
* Implement validation and fallback mechanisms

Decision: Validation and fallback
Tradeoffs: More complex implementation but significantly improved reliability and user experience. Users always get actionable suggestions even when AI is down.

## 4. Tradeoffs

* Asynchronous processing adds complexity in terms of error handling and debugging but improves user experience
* SSE adds connection management overhead but eliminates polling network traffic
* AI provides flexible reasoning but is nondeterministic; fallback to rules improves predictability
* Runtime strategy switching increases flexibility but adds slight complexity to configuration
* Separation of frontend/backend allows independent scaling but requires careful API design
* Idempotency checks protect against duplicates but add database queries to suggestion generation