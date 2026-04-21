package com.railtrack.railtrack_backend.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class StatsResponse {
    private long totalTracks;
    private long totalIncidents;
    private long openIncidents;
    private long tracksInMaintenance;
    private Map<String, Long> tracksByStatus;
    private Map<String, Long> incidentsBySeverity;
    private List<IncidentResponse> recentOpenIncidents;
}
