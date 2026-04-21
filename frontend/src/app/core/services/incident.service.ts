import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Incident, IncidentDto } from '../models/incident.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class IncidentService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/incidents';

  getAll(page = 0, size = 10) {
    const params = new HttpParams().set('page', page).set('size', size).set('sort', 'reportedAt,desc');
    return this.http.get<Page<Incident>>(this.apiUrl, { params });
  }

  create(dto: IncidentDto) {
    return this.http.post<Incident>(this.apiUrl, dto);
  }

  update(id: number, dto: IncidentDto) {
    return this.http.put<Incident>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number) {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
