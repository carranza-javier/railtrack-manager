import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: '',
    loadComponent: () =>
      import('./layout/shell/shell.component').then((m) => m.ShellComponent),
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'tracks', pathMatch: 'full' },
      {
        path: 'tracks',
        loadComponent: () =>
          import('./features/tracks/tracks.component').then((m) => m.TracksComponent),
      },
      {
        path: 'incidents',
        loadComponent: () =>
          import('./features/incidents/incidents.component').then((m) => m.IncidentsComponent),
      },
    ],
  },
  { path: '**', redirectTo: '' },
];
