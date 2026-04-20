export type IncidentSeverity = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
export type IncidentStatus = 'OPEN' | 'IN_PROGRESS' | 'RESOLVED';

export interface Incident {
  id: number;
  title: string;
  description: string;
  severity: IncidentSeverity;
  status: IncidentStatus;
  reportedAt: string;
  resolvedAt: string | null;
  trackSegmentId: number;
  trackSegmentName: string;
}

export interface IncidentDto {
  title: string;
  description: string;
  severity: IncidentSeverity;
  status: IncidentStatus;
  trackSegmentId: number;
}
