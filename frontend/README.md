# RailTrack Manager — Frontend

Angular 21 SPA for the RailTrack Manager application.

## Requirements

- Node.js 18+
- npm 9+

## Running locally

```bash
npm install
npm start
```

Opens at `http://localhost:4200`. The app expects the backend running at `http://localhost:8080`.

## Building for production

```bash
npm run build
```

Output goes to `dist/frontend/`.

## Project structure

```
src/app/
├── core/
│   ├── guards/         # Auth guard (protects routes)
│   ├── interceptors/   # JWT interceptor (attaches Bearer token)
│   ├── models/         # TypeScript interfaces
│   └── services/       # AuthService
├── features/
│   ├── auth/login/     # Login page
│   ├── tracks/         # Track segments feature
│   └── incidents/      # Incidents feature
└── layout/
    └── shell/          # App shell: sidenav + toolbar
```

## Authentication flow

1. Unauthenticated users are redirected to `/login`
2. On successful login, the JWT is stored in `localStorage`
3. The HTTP interceptor attaches `Authorization: Bearer <token>` to every request
4. Logout clears storage and redirects back to `/login`

## Running tests

```bash
npm test
```
