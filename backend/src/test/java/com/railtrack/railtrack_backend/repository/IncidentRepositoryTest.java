package com.railtrack.railtrack_backend.repository;

import com.railtrack.railtrack_backend.model.Incident;
import com.railtrack.railtrack_backend.model.TrackSegment;
import com.railtrack.railtrack_backend.model.enums.IncidentSeverity;
import com.railtrack.railtrack_backend.model.enums.IncidentStatus;
import com.railtrack.railtrack_backend.model.enums.TrackStatus;
import com.railtrack.railtrack_backend.model.enums.TrackType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class IncidentRepositoryTest {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private TrackSegmentRepository trackSegmentRepository;

    private TrackSegment segment;

    @BeforeEach
    void setUp() {
        segment = trackSegmentRepository.save(TrackSegment.builder()
                .name("Segment A").lineCode("LC-01")
                .trackType(TrackType.MAIN).status(TrackStatus.OPERATIONAL)
                .build());
    }

    private Incident buildIncident(String title, IncidentSeverity severity, IncidentStatus status) {
        return Incident.builder()
                .title(title)
                .description("Description")
                .severity(severity)
                .status(status)
                .trackSegment(segment)
                .build();
    }

    @Test
    void givenIncidentsForSegment_whenFindByTrackSegmentId_thenReturnsOnlyRelatedIncidents() {
        incidentRepository.save(buildIncident("Inc A", IncidentSeverity.LOW, IncidentStatus.OPEN));
        incidentRepository.save(buildIncident("Inc B", IncidentSeverity.HIGH, IncidentStatus.OPEN));

        TrackSegment other = trackSegmentRepository.save(TrackSegment.builder()
                .name("Segment B").lineCode("LC-02")
                .trackType(TrackType.SECONDARY).status(TrackStatus.OPERATIONAL)
                .build());
        incidentRepository.save(Incident.builder()
                .title("Other Inc").severity(IncidentSeverity.LOW)
                .status(IncidentStatus.OPEN).trackSegment(other).build());

        List<Incident> result = incidentRepository.findByTrackSegmentId(segment.getId());

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(i -> i.getTrackSegment().getId().equals(segment.getId())));
    }

    @Test
    void givenIncidentsWithDifferentSeverities_whenFindBySeverity_thenReturnsOnlyMatchingSeverity() {
        incidentRepository.save(buildIncident("Inc A", IncidentSeverity.CRITICAL, IncidentStatus.OPEN));
        incidentRepository.save(buildIncident("Inc B", IncidentSeverity.CRITICAL, IncidentStatus.IN_PROGRESS));
        incidentRepository.save(buildIncident("Inc C", IncidentSeverity.LOW, IncidentStatus.OPEN));

        List<Incident> result = incidentRepository.findBySeverity(IncidentSeverity.CRITICAL);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(i -> i.getSeverity() == IncidentSeverity.CRITICAL));
    }

    @Test
    void givenIncidentsWithDifferentStatuses_whenCountByStatus_thenReturnsCorrectCount() {
        incidentRepository.save(buildIncident("Inc A", IncidentSeverity.LOW, IncidentStatus.OPEN));
        incidentRepository.save(buildIncident("Inc B", IncidentSeverity.MEDIUM, IncidentStatus.OPEN));
        incidentRepository.save(buildIncident("Inc C", IncidentSeverity.HIGH, IncidentStatus.RESOLVED));

        long openCount = incidentRepository.countByStatus(IncidentStatus.OPEN);
        long resolvedCount = incidentRepository.countByStatus(IncidentStatus.RESOLVED);

        assertEquals(2L, openCount);
        assertEquals(1L, resolvedCount);
    }

    @Test
    void givenIncidentsWithDifferentSeverities_whenCountBySeverity_thenReturnsCorrectCount() {
        incidentRepository.save(buildIncident("Inc A", IncidentSeverity.HIGH, IncidentStatus.OPEN));
        incidentRepository.save(buildIncident("Inc B", IncidentSeverity.HIGH, IncidentStatus.OPEN));
        incidentRepository.save(buildIncident("Inc C", IncidentSeverity.LOW, IncidentStatus.OPEN));

        long highCount = incidentRepository.countBySeverity(IncidentSeverity.HIGH);
        long lowCount = incidentRepository.countBySeverity(IncidentSeverity.LOW);

        assertEquals(2L, highCount);
        assertEquals(1L, lowCount);
    }

    @Test
    void givenOpenAndResolvedIncidents_whenFindByStatusIn_thenReturnsOnlyOpenAndInProgress() {
        incidentRepository.save(buildIncident("Open Inc", IncidentSeverity.LOW, IncidentStatus.OPEN));
        incidentRepository.save(buildIncident("Progress Inc", IncidentSeverity.MEDIUM, IncidentStatus.IN_PROGRESS));
        incidentRepository.save(buildIncident("Resolved Inc", IncidentSeverity.HIGH, IncidentStatus.RESOLVED));

        List<Incident> result = incidentRepository.findByStatusInOrderByReportedAtDesc(
                List.of(IncidentStatus.OPEN, IncidentStatus.IN_PROGRESS),
                PageRequest.of(0, 10));

        assertEquals(2, result.size());
        assertTrue(result.stream().noneMatch(i -> i.getStatus() == IncidentStatus.RESOLVED));
    }

    @Test
    void givenMoreThanFiveOpenIncidents_whenFindByStatusInWithPageLimit_thenReturnsAtMostFive() {
        for (int i = 1; i <= 7; i++) {
            incidentRepository.save(buildIncident("Inc " + i, IncidentSeverity.LOW, IncidentStatus.OPEN));
        }

        List<Incident> result = incidentRepository.findByStatusInOrderByReportedAtDesc(
                List.of(IncidentStatus.OPEN, IncidentStatus.IN_PROGRESS),
                PageRequest.of(0, 5));

        assertEquals(5, result.size());
    }

    @Test
    void givenSavedIncident_whenFindById_thenReportedAtIsSetAutomatically() {
        Incident saved = incidentRepository.save(
                buildIncident("Inc A", IncidentSeverity.LOW, IncidentStatus.OPEN));

        Incident found = incidentRepository.findById(saved.getId()).orElseThrow();

        assertNotNull(found.getReportedAt());
    }
}
