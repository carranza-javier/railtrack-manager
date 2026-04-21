import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { TrackSegment, TrackSegmentDto } from '../models/track-segment.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class TrackSegmentService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/tracks';

  getAll(page = 0, size = 10) {
    const params = new HttpParams().set('page', page).set('size', size).set('sort', 'name,asc');
    return this.http.get<Page<TrackSegment>>(this.apiUrl, { params });
  }

  getList() {
    const params = new HttpParams().set('page', 0).set('size', 100);
    return this.http.get<Page<TrackSegment>>(this.apiUrl, { params });
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
