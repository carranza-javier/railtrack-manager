import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Incident, IncidentDto } from '../models/incident.model';

@Injectable({ providedIn: 'root' })
export class IncidentService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/incidents';

  getAll() {
    return this.http.get<Incident[]>(this.apiUrl);
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
