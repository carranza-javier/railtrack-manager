package com.railtrack.railtrack_backend.service;

import com.railtrack.railtrack_backend.dto.IncidentResponse;
import com.railtrack.railtrack_backend.dto.StatsResponse;
import com.railtrack.railtrack_backend.model.enums.IncidentSeverity;
import com.railtrack.railtrack_backend.model.enums.IncidentStatus;
import com.railtrack.railtrack_backend.model.enums.TrackStatus;
import com.railtrack.railtrack_backend.repository.IncidentRepository;
import com.railtrack.railtrack_backend.repository.TrackSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final TrackSegmentRepository trackRepo;
    private final IncidentRepository incidentRepo;
    private final IncidentService incidentService;

    public StatsResponse getStats() {
        List<IncidentResponse> recentOpen = incidentRepo
                .findByStatusInOrderByReportedAtDesc(
                        List.of(IncidentStatus.OPEN, IncidentStatus.IN_PROGRESS),
                        PageRequest.of(0, 5))
                .stream()
                .map(incidentService::toResponse)
                .toList();

        return StatsResponse.builder()
                .totalTracks(trackRepo.count())
                .totalIncidents(incidentRepo.count())
                .openIncidents(incidentRepo.countByStatus(IncidentStatus.OPEN)
                        + incidentRepo.countByStatus(IncidentStatus.IN_PROGRESS))
                .tracksInMaintenance(trackRepo.countByStatus(TrackStatus.MAINTENANCE))
                .tracksByStatus(Map.of(
                        "OPERATIONAL", trackRepo.countByStatus(TrackStatus.OPERATIONAL),
                        "MAINTENANCE", trackRepo.countByStatus(TrackStatus.MAINTENANCE),
                        "CLOSED", trackRepo.countByStatus(TrackStatus.CLOSED)
                ))
                .incidentsBySeverity(Map.of(
                        "LOW", incidentRepo.countBySeverity(IncidentSeverity.LOW),
                        "MEDIUM", incidentRepo.countBySeverity(IncidentSeverity.MEDIUM),
                        "HIGH", incidentRepo.countBySeverity(IncidentSeverity.HIGH),
                        "CRITICAL", incidentRepo.countBySeverity(IncidentSeverity.CRITICAL)
                ))
                .recentOpenIncidents(recentOpen)
                .build();
    }
}
