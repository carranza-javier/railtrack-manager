# RailTrack Manager

A railway infrastructure management application for tracking segments and incidents across a rail network.

Built as a technical portfolio project using a modern full-stack: Angular 21 on the frontend and Spring Boot 3 on the backend.

## What it does

- Manage **track segments** — lines, types, operational status, maintenance history
- Track **incidents** — signal failures, geometry deviations, equipment malfunctions
- JWT-based authentication with role support
- Seeded with real Swiss railway data (Bern–Olten, Lausanne–Yverdon, Zürich Depot)

## Tech stack

| Layer     | Technology                                      |
|-----------|-------------------------------------------------|
| Frontend  | Angular 21, Angular Material, standalone components |
| Backend   | Spring Boot 3.5, Java 21, Spring Security + JWT |
| Database  | H2 in-memory (development)                      |
| Build     | Maven (backend), npm (frontend)                 |

## Project structure

```
railtrack-manager/
├── backend/    # Spring Boot REST API
└── frontend/   # Angular SPA
```

## Quick start

**Requirements:** Java 21, Node.js 18+, Maven

```bash
# Start the backend (port 8080)
cd backend
./mvnw spring-boot:run

# Start the frontend (port 4200)
cd frontend
npm install
npm start
```

Open `http://localhost:4200` — you will be redirected to the login page.

**Default credentials** (seeded on startup):
> Register a new account via `POST /api/auth/register` or use the login form after registering through the UI.

## API

The backend exposes a REST API at `http://localhost:8080/api`.

| Method | Endpoint                    | Auth | Description              |
|--------|-----------------------------|------|--------------------------|
| POST   | `/api/auth/register`        | No   | Create account           |
| POST   | `/api/auth/login`           | No   | Get JWT token            |
| GET    | `/api/track-segments`       | Yes  | List track segments      |
| POST   | `/api/track-segments`       | Yes  | Create track segment     |
| PUT    | `/api/track-segments/{id}`  | Yes  | Update track segment     |
| DELETE | `/api/track-segments/{id}`  | Yes  | Delete track segment     |
| GET    | `/api/incidents`            | Yes  | List incidents           |
| POST   | `/api/incidents`            | Yes  | Create incident          |
| PUT    | `/api/incidents/{id}`       | Yes  | Update incident          |
| DELETE | `/api/incidents/{id}`       | Yes  | Delete incident          |

H2 console available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:railtrackdb`).
