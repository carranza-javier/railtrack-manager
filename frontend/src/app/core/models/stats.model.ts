import { Incident } from './incident.model';

export interface Stats {
  totalTracks: number;
  totalIncidents: number;
  openIncidents: number;
  tracksInMaintenance: number;
  tracksByStatus: Record<string, number>;
  incidentsBySeverity: Record<string, number>;
  recentOpenIncidents: Incident[];
}
