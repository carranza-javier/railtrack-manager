import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { TrackSegment, TrackSegmentDto } from '../../../core/models/track-segment.model';

@Component({
  selector: 'app-track-form-dialog',
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
  ],
  templateUrl: './track-form-dialog.component.html',
})
export class TrackFormDialogComponent {
  private readonly fb = inject(FormBuilder);
  readonly ref = inject(MatDialogRef<TrackFormDialogComponent>);
  readonly data = inject<TrackSegment | null>(MAT_DIALOG_DATA);

  readonly trackTypes = ['MAIN', 'SECONDARY', 'DEPOT'];
  readonly trackStatuses = ['OPERATIONAL', 'MAINTENANCE', 'CLOSED'];

  readonly form = this.fb.group({
    name: [this.data?.name ?? '', [Validators.required]],
    lineCode: [this.data?.lineCode ?? '', [Validators.required]],
    trackType: [this.data?.trackType ?? '', [Validators.required]],
    status: [this.data?.status ?? '', [Validators.required]],
    length: [this.data?.length ?? null],
    lastMaintenanceDate: [
      this.data?.lastMaintenanceDate ? new Date(this.data.lastMaintenanceDate) : null,
    ],
  });

  get isEdit() {
    return !!this.data;
  }

  submit() {
    if (this.form.invalid) return;
    const raw = this.form.getRawValue();
    const dto: TrackSegmentDto = {
      name: raw.name!,
      lineCode: raw.lineCode!,
      trackType: raw.trackType as any,
      status: raw.status as any,
      length: raw.length ?? undefined,
      lastMaintenanceDate: raw.lastMaintenanceDate
        ? (raw.lastMaintenanceDate as Date).toISOString().split('T')[0]
        : undefined,
    };
    this.ref.close(dto);
  }
}
