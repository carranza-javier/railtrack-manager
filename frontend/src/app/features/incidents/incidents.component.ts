import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-incidents',
  imports: [MatCardModule],
  template: `
    <mat-card>
      <mat-card-header>
        <mat-card-title>Incidents</mat-card-title>
        <mat-card-subtitle>Manage railway incidents</mat-card-subtitle>
      </mat-card-header>
      <mat-card-content>
        <p style="padding-top: 16px; color: var(--mat-sys-on-surface-variant)">
          Full CRUD coming in Day 4.
        </p>
      </mat-card-content>
    </mat-card>
  `,
})
export class IncidentsComponent {}
