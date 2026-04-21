import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Incident, IncidentDto } from '../../../core/models/incident.model';
import { TrackSegment } from '../../../core/models/track-segment.model';
import { TrackSegmentService } from '../../../core/services/track-segment.service';

@Component({
  selector: 'app-incident-form-dialog',
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './incident-form-dialog.component.html',
})
export class IncidentFormDialogComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly trackService = inject(TrackSegmentService);
  readonly ref = inject(MatDialogRef<IncidentFormDialogComponent>);
  readonly data = inject<Incident | null>(MAT_DIALOG_DATA);

  readonly severities = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];
  readonly statuses = ['OPEN', 'IN_PROGRESS', 'RESOLVED'];
  readonly tracks = signal<TrackSegment[]>([]);

  readonly form = this.fb.group({
    title: [this.data?.title ?? '', [Validators.required]],
    description: [this.data?.description ?? ''],
    severity: [this.data?.severity ?? '', [Validators.required]],
    status: [this.data?.status ?? '', [Validators.required]],
    trackSegmentId: [this.data?.trackSegmentId ?? null, [Validators.required]],
  });

  get isEdit() {
    return !!this.data;
  }

  ngOnInit() {
    this.trackService.getAll().subscribe((t) => this.tracks.set(t));
  }

  submit() {
    if (this.form.invalid) return;
    const raw = this.form.getRawValue();
    const dto: IncidentDto = {
      title: raw.title!,
      description: raw.description ?? '',
      severity: raw.severity as any,
      status: raw.status as any,
      trackSegmentId: raw.trackSegmentId!,
    };
    this.ref.close(dto);
  }
}
