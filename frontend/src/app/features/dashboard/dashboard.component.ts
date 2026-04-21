import { Component, OnInit, inject, signal } from '@angular/core';
import { LowerCasePipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Stats } from '../../core/models/stats.model';
import { StatsService } from '../../core/services/stats.service';
import { Incident } from '../../core/models/incident.model';

@Component({
  selector: 'app-dashboard',
  imports: [
    LowerCasePipe,
    MatCardModule,
    MatIconModule,
    MatTableModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  private readonly statsService = inject(StatsService);

  readonly stats = signal<Stats | null>(null);
  readonly loading = signal(true);

  readonly incidentColumns = ['title', 'severity', 'segment', 'reportedAt'];

  ngOnInit() {
    this.statsService.getStats().subscribe({
      next: (data) => { this.stats.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  formatDate(dateStr: string): string {
    return new Date(dateStr).toLocaleDateString('en-CH', {
      day: '2-digit', month: 'short', year: 'numeric',
    });
  }

  statusEntries(map: Record<string, number>): { key: string; value: number }[] {
    return Object.entries(map).map(([key, value]) => ({ key, value }));
  }

  recentIncidents(): Incident[] {
    return this.stats()?.recentOpenIncidents ?? [];
  }
}
