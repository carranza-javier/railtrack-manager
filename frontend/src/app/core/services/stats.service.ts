import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Stats } from '../models/stats.model';

@Injectable({ providedIn: 'root' })
export class StatsService {
  private readonly http = inject(HttpClient);

  getStats() {
    return this.http.get<Stats>('http://localhost:8080/api/stats');
  }
}
