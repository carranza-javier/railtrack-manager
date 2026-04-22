package com.railtrack.railtrack_backend.service;

import com.railtrack.railtrack_backend.dto.IncidentResponse;
import com.railtrack.railtrack_backend.dto.StatsResponse;
import com.railtrack.railtrack_backend.model.Incident;
import com.railtrack.railtrack_backend.model.TrackSegment;
import com.railtrack.railtrack_backend.model.enums.IncidentSeverity;
import com.railtrack.railtrack_backend.model.enums.IncidentStatus;
import com.railtrack.railtrack_backend.model.enums.TrackStatus;
import com.railtrack.railtrack_backend.model.enums.TrackType;
import com.railtrack.railtrack_backend.repository.IncidentRepository;
import com.railtrack.railtrack_backend.repository.TrackSegmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private TrackSegmentRepository trackRepo;

    @Mock
    private IncidentRepository incidentRepo;

    @Mock
    private IncidentService incidentService;

    @InjectMocks
    private StatsService service;

    private void stubZeroDefaults() {
        when(trackRepo.count()).thenReturn(0L);
        when(incidentRepo.count()).thenReturn(0L);
        when(incidentRepo.countByStatus(any())).thenReturn(0L);
        when(trackRepo.countByStatus(any())).thenReturn(0L);
        when(incidentRepo.countBySeverity(any())).thenReturn(0L);
        when(incidentRepo.findByStatusInOrderByReportedAtDesc(any(), any())).thenReturn(List.of());
    }

    private TrackSegment buildSegment() {
        return TrackSegment.builder()
                .id(1L).name("Seg A").lineCode("LC-01")
                .trackType(TrackType.MAIN).status(TrackStatus.OPERATIONAL)
                .build();
    }

    private Incident buildIncident(Long id, TrackSegment segment) {
        return Incident.builder()
                .id(id).title("Incident " + id)
                .severity(IncidentSeverity.MEDIUM).status(IncidentStatus.OPEN)
                .reportedAt(LocalDateTime.now()).trackSegment(segment)
                .build();
    }

    @Test
    void givenRepositoryData_whenGetStats_thenReturnsTotalTracksAndIncidents() {
        stubZeroDefaults();
        when(trackRepo.count()).thenReturn(10L);
        when(incidentRepo.count()).thenReturn(7L);

        StatsResponse result = service.getStats();

        assertEquals(10L, result.getTotalTracks());
        assertEquals(7L, result.getTotalIncidents());
    }

    @Test
    void givenOpenAndInProgressIncidents_whenGetStats_thenSumsOpenIncidentsCorrectly() {
        stubZeroDefaults();
        when(incidentRepo.countByStatus(IncidentStatus.OPEN)).thenReturn(3L);
        when(incidentRepo.countByStatus(IncidentStatus.IN_PROGRESS)).thenReturn(2L);

        StatsResponse result = service.getStats();

        assertEquals(5L, result.getOpenIncidents());
    }

    @Test
    void givenMaintenanceTracks_whenGetStats_thenReturnsTracksInMaintenanceCorrectly() {
        stubZeroDefaults();
        when(trackRepo.countByStatus(TrackStatus.MAINTENANCE)).thenReturn(4L);

        StatsResponse result = service.getStats();

        assertEquals(4L, result.getTracksInMaintenance());
        assertEquals(4L, result.getTracksByStatus().get("MAINTENANCE"));
    }

    @Test
    void givenTracksByStatus_whenGetStats_thenReturnsTracksByStatusMap() {
        stubZeroDefaults();
        when(trackRepo.countByStatus(TrackStatus.OPERATIONAL)).thenReturn(5L);
        when(trackRepo.countByStatus(TrackStatus.MAINTENANCE)).thenReturn(3L);
        when(trackRepo.countByStatus(TrackStatus.CLOSED)).thenReturn(2L);

        StatsResponse result = service.getStats();

        assertEquals(5L, result.getTracksByStatus().get("OPERATIONAL"));
        assertEquals(3L, result.getTracksByStatus().get("MAINTENANCE"));
        assertEquals(2L, result.getTracksByStatus().get("CLOSED"));
    }

    @Test
    void givenIncidentsBySeverity_whenGetStats_thenReturnsIncidentsBySeverityMap() {
        stubZeroDefaults();
        when(incidentRepo.countBySeverity(IncidentSeverity.LOW)).thenReturn(1L);
        when(incidentRepo.countBySeverity(IncidentSeverity.MEDIUM)).thenReturn(2L);
        when(incidentRepo.countBySeverity(IncidentSeverity.HIGH)).thenReturn(3L);
        when(incidentRepo.countBySeverity(IncidentSeverity.CRITICAL)).thenReturn(4L);

        StatsResponse result = service.getStats();

        assertEquals(1L, result.getIncidentsBySeverity().get("LOW"));
        assertEquals(2L, result.getIncidentsBySeverity().get("MEDIUM"));
        assertEquals(3L, result.getIncidentsBySeverity().get("HIGH"));
        assertEquals(4L, result.getIncidentsBySeverity().get("CRITICAL"));
    }

    @Test
    void givenRecentOpenIncidents_whenGetStats_thenMapsIncidentsToResponses() {
        TrackSegment segment = buildSegment();
        List<Incident> recentIncidents = List.of(
                buildIncident(1L, segment),
                buildIncident(2L, segment));

        IncidentResponse response1 = IncidentResponse.builder().id(1L).title("Incident 1").build();
        IncidentResponse response2 = IncidentResponse.builder().id(2L).title("Incident 2").build();

        stubZeroDefaults();
        when(incidentRepo.findByStatusInOrderByReportedAtDesc(
                eq(List.of(IncidentStatus.OPEN, IncidentStatus.IN_PROGRESS)), any()))
                .thenReturn(recentIncidents);
        when(incidentService.toResponse(recentIncidents.get(0))).thenReturn(response1);
        when(incidentService.toResponse(recentIncidents.get(1))).thenReturn(response2);

        StatsResponse result = service.getStats();

        assertEquals(2, result.getRecentOpenIncidents().size());
        assertEquals("Incident 1", result.getRecentOpenIncidents().get(0).getTitle());
        assertEquals("Incident 2", result.getRecentOpenIncidents().get(1).getTitle());
    }

    @Test
    void givenNoIncidents_whenGetStats_thenReturnsEmptyRecentOpenIncidents() {
        stubZeroDefaults();

        StatsResponse result = service.getStats();

        assertNotNull(result.getRecentOpenIncidents());
        assertTrue(result.getRecentOpenIncidents().isEmpty());
    }
}
