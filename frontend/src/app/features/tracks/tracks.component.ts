import { Component, OnInit, inject, signal } from '@angular/core';
import { LowerCasePipe } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialog } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TrackSegment } from '../../core/models/track-segment.model';
import { TrackSegmentService } from '../../core/services/track-segment.service';
import { TrackFormDialogComponent } from './track-form-dialog/track-form-dialog.component';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-tracks',
  imports: [
    LowerCasePipe,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
  ],
  templateUrl: './tracks.component.html',
  styleUrl: './tracks.component.scss',
})
export class TracksComponent implements OnInit {
  private readonly service = inject(TrackSegmentService);
  private readonly dialog = inject(MatDialog);

  readonly tracks = signal<TrackSegment[]>([]);
  readonly loading = signal(true);

  readonly columns = ['name', 'lineCode', 'trackType', 'status', 'length', 'lastMaintenance', 'actions'];

  ngOnInit() {
    this.load();
  }

  load() {
    this.loading.set(true);
    this.service.getAll().subscribe({
      next: (data) => { this.tracks.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  openCreate() {
    this.dialog.open(TrackFormDialogComponent, { data: null, width: '520px' })
      .afterClosed().subscribe((dto) => {
        if (dto) this.service.create(dto).subscribe(() => this.load());
      });
  }

  openEdit(track: TrackSegment) {
    this.dialog.open(TrackFormDialogComponent, { data: track, width: '520px' })
      .afterClosed().subscribe((dto) => {
        if (dto) this.service.update(track.id, dto).subscribe(() => this.load());
      });
  }

  openDelete(track: TrackSegment) {
    this.dialog.open(ConfirmDialogComponent, {
      data: { title: 'Delete segment', message: `Delete "${track.name}"? This cannot be undone.` },
    }).afterClosed().subscribe((confirmed) => {
      if (confirmed) this.service.delete(track.id).subscribe(() => this.load());
    });
  }

  statusColor(status: string): string {
    return { OPERATIONAL: 'primary', MAINTENANCE: 'accent', CLOSED: 'warn' }[status] ?? '';
  }
}
