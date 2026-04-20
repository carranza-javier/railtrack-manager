# RailTrack Manager — Backend

Spring Boot 3 REST API for the RailTrack Manager application.

## Requirements

- Java 21
- Maven (or use the included `./mvnw` wrapper)

## Running locally

```bash
./mvnw spring-boot:run
```

The server starts on `http://localhost:8080`.

## Configuration

All settings live in `src/main/resources/application.properties`.

| Property | Default | Description |
|----------|---------|-------------|
| `spring.datasource.url` | H2 in-memory | Database connection |
| `jwt.secret` | (hex string) | HMAC-SHA256 signing key |
| `jwt.expiration` | `86400000` | Token TTL in ms (24 hours) |

The H2 console is available at `http://localhost:8080/h2-console` with JDBC URL `jdbc:h2:mem:railtrackdb`.

## Package structure

```
com.railtrack/
├── controller/     # REST endpoints
├── service/        # Business logic
├── repository/     # Spring Data JPA interfaces
├── model/          # JPA entities + enums
├── dto/            # Request and response objects
├── security/       # JWT filter, util, UserDetailsService
├── config/         # SecurityConfig, DataLoader (seed data)
└── exception/      # GlobalExceptionHandler, ResourceNotFoundException
```

## Authentication

The API uses JWT Bearer tokens. All endpoints under `/api/track-segments` and `/api/incidents` require a valid token.

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "secret123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "secret123"}'
```

The response contains a `token` field. Pass it as `Authorization: Bearer <token>` on subsequent requests.

## Seed data

On startup, `DataLoader` inserts three track segments and three incidents:

| Segment | Line | Status |
|---------|------|--------|
| Bern – Olten | IC1 | Operational |
| Lausanne – Yverdon | RE4 | Maintenance |
| Zürich Depot West | DEP-ZRH | Operational |

## Running tests

```bash
./mvnw test
```
