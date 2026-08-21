# Idlemania

Airport-themed idle game with a Spring Boot API, Angular client, and PostgreSQL persistence.

## Requirements

- Java 25
- Node.js 24 and npm 11
- Docker with Compose (for PostgreSQL)

## Run locally

```bash
docker compose up -d
cd backend && ./mvnw spring-boot:run
```

In a second terminal:

```bash
cd frontend
npm ci
npm start
```

The Angular development server proxies `/api` requests to the backend.

## Verify

```bash
cd backend && ./mvnw verify
cd frontend && npm ci && npm run check && npm run build
```
