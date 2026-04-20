export type TrackStatus = 'OPERATIONAL' | 'MAINTENANCE' | 'CLOSED';
export type TrackType = 'MAIN' | 'SECONDARY' | 'DEPOT';

export interface TrackSegment {
  id: number;
  name: string;
  lineCode: string;
  trackType: TrackType;
  status: TrackStatus;
  length: number;
  lastMaintenanceDate: string;
  createdAt: string;
  updatedAt: string;
}

export interface TrackSegmentDto {
  name: string;
  lineCode: string;
  trackType: TrackType;
  status: TrackStatus;
  length: number;
  lastMaintenanceDate: string;
}
