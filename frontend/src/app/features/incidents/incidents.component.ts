import { Component, OnInit, inject, signal } from '@angular/core';
import { LowerCasePipe } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Incident } from '../../core/models/incident.model';
import { IncidentService } from '../../core/services/incident.service';
import { IncidentFormDialogComponent } from './incident-form-dialog/incident-form-dialog.component';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-incidents',
  imports: [
    LowerCasePipe,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
  ],
  templateUrl: './incidents.component.html',
  styleUrl: './incidents.component.scss',
})
export class IncidentsComponent implements OnInit {
  private readonly service = inject(IncidentService);
  private readonly dialog = inject(MatDialog);

  readonly incidents = signal<Incident[]>([]);
  readonly loading = signal(true);

  readonly columns = ['title', 'severity', 'status', 'segment', 'reportedAt', 'actions'];

  ngOnInit() {
    this.load();
  }

  load() {
    this.loading.set(true);
    this.service.getAll().subscribe({
      next: (data) => { this.incidents.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  openCreate() {
    this.dialog.open(IncidentFormDialogComponent, { data: null, width: '520px' })
      .afterClosed().subscribe((dto) => {
        if (dto) this.service.create(dto).subscribe(() => this.load());
      });
  }

  openEdit(incident: Incident) {
    this.dialog.open(IncidentFormDialogComponent, { data: incident, width: '520px' })
      .afterClosed().subscribe((dto) => {
        if (dto) this.service.update(incident.id, dto).subscribe(() => this.load());
      });
  }

  openDelete(incident: Incident) {
    this.dialog.open(ConfirmDialogComponent, {
      data: { title: 'Delete incident', message: `Delete "${incident.title}"? This cannot be undone.` },
    }).afterClosed().subscribe((confirmed) => {
      if (confirmed) this.service.delete(incident.id).subscribe(() => this.load());
    });
  }

  formatDate(dateStr: string): string {
    return new Date(dateStr).toLocaleDateString('en-CH', { day: '2-digit', month: 'short', year: 'numeric' });
  }
}
