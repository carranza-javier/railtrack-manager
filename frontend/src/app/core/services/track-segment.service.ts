import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TrackSegment, TrackSegmentDto } from '../models/track-segment.model';

@Injectable({ providedIn: 'root' })
export class TrackSegmentService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/tracks';

  getAll() {
    return this.http.get<TrackSegment[]>(this.apiUrl);
  }

  create(dto: TrackSegmentDto) {
    return this.http.post<TrackSegment>(this.apiUrl, dto);
  }

  update(id: number, dto: TrackSegmentDto) {
    return this.http.put<TrackSegment>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number) {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
