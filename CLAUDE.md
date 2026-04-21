# RailTrack Manager

A railway infrastructure management application built with Angular 21 + Spring Boot 3.
Developed as a technical portfolio project targeting SBB/VSTG interview environments.

## Tech Stack
- **Backend:** Spring Boot 3.4, Java 21, Maven
- **Frontend:** Angular 21, standalone components, SCSS
- **Database:** H2 (development), Spring Data JPA
- **Security:** Spring Security + JWT

## Project Structure
- `/backend` → Spring Boot REST API (`com.railtrack`)
- `/frontend` → Angular SPA

## Backend Conventions
- Package structure: `controller`, `service`, `repository`, `model`, `dto`, `exception`
- DTOs for all requests/responses (never expose entities directly)
- `ResponseEntity<?>` in all controllers
- Global error handling with `@ControllerAdvice`

## Frontend Conventions
- Standalone components only
- Angular Material for UI components
- Services in `/core/services`
- Models/interfaces in `/core/models`

## Progress
- [x] Day 1: JPA entities + basic REST endpoints
- [x] Day 2: Spring Security + JWT authentication
- [x] Day 3: Angular base structure + routing
- [x] Day 4: Full CRUD with frontend/backend integration
- [x] Day 5: Dashboard + statistics + final polish