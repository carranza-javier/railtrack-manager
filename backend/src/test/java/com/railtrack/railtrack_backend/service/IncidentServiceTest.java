package com.railtrack.railtrack_backend.service;

import com.railtrack.railtrack_backend.dto.IncidentDto;
import com.railtrack.railtrack_backend.dto.IncidentResponse;
import com.railtrack.railtrack_backend.exception.ResourceNotFoundException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private TrackSegmentRepository trackSegmentRepository;

    @InjectMocks
    private IncidentService service;

    private TrackSegment buildSegment() {
        return TrackSegment.builder()
                .id(1L)
                .name("Segment A")
                .lineCode("LC-01")
                .trackType(TrackType.MAIN)
                .status(TrackStatus.OPERATIONAL)
                .build();
    }

    private Incident buildIncident(TrackSegment segment) {
        return Incident.builder()
                .id(1L)
                .title("Signal failure")
                .description("Red signal malfunction")
                .severity(IncidentSeverity.HIGH)
                .status(IncidentStatus.OPEN)
                .reportedAt(LocalDateTime.now())
                .trackSegment(segment)
                .build();
    }

    private IncidentDto buildDto() {
        IncidentDto dto = new IncidentDto();
        dto.setTitle("Signal failure");
        dto.setDescription("Red signal malfunction");
        dto.setSeverity(IncidentSeverity.HIGH);
        dto.setStatus(IncidentStatus.OPEN);
        dto.setTrackSegmentId(1L);
        return dto;
    }

    @Test
    void givenExistingIncidents_whenFindAll_thenReturnsPageOfResponses() {
        TrackSegment segment = buildSegment();
        Page<Incident> page = new PageImpl<>(List.of(buildIncident(segment)));
        when(incidentRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<IncidentResponse> result = service.findAll(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Signal failure", result.getContent().get(0).getTitle());
        assertEquals(IncidentSeverity.HIGH, result.getContent().get(0).getSeverity());
    }

    @Test
    void givenExistingId_whenFindById_thenReturnsResponse() {
        TrackSegment segment = buildSegment();
        when(incidentRepository.findById(1L)).thenReturn(Optional.of(buildIncident(segment)));

        IncidentResponse result = service.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Signal failure", result.getTitle());
        assertEquals(1L, result.getTrackSegmentId());
        assertEquals("Segment A", result.getTrackSegmentName());
    }

    @Test
    void givenNonExistingId_whenFindById_thenThrowsResourceNotFoundException() {
        when(incidentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void givenValidDto_whenCreate_thenReturnsCreatedResponse() {
        TrackSegment segment = buildSegment();
        when(trackSegmentRepository.findById(1L)).thenReturn(Optional.of(segment));
        when(incidentRepository.save(any(Incident.class))).thenReturn(buildIncident(segment));

        IncidentResponse result = service.create(buildDto());

        assertEquals("Signal failure", result.getTitle());
        assertEquals(IncidentSeverity.HIGH, result.getSeverity());
        assertEquals(1L, result.getTrackSegmentId());
        verify(incidentRepository, times(1)).save(any(Incident.class));
    }

    @Test
    void givenNonExistingTrackSegment_whenCreate_thenThrowsResourceNotFoundException() {
        when(trackSegmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.create(buildDto()));
        verify(incidentRepository, never()).save(any());
    }

    @Test
    void givenExistingId_whenUpdate_thenReturnsUpdatedResponse() {
        TrackSegment segment = buildSegment();
        Incident updated = buildIncident(segment);
        updated.setTitle("Track switch broken");

        when(incidentRepository.findById(1L)).thenReturn(Optional.of(buildIncident(segment)));
        when(trackSegmentRepository.findById(1L)).thenReturn(Optional.of(segment));
        when(incidentRepository.save(any(Incident.class))).thenReturn(updated);

        IncidentDto dto = buildDto();
        dto.setTitle("Track switch broken");

        IncidentResponse result = service.update(1L, dto);

        assertEquals("Track switch broken", result.getTitle());
        verify(incidentRepository, times(1)).save(any(Incident.class));
    }

    @Test
    void givenNonExistingId_whenUpdate_thenThrowsResourceNotFoundException() {
        when(incidentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(99L, buildDto()));
        verify(incidentRepository, never()).save(any());
    }

    @Test
    void givenExistingId_whenDelete_thenDeleteByIdIsCalled() {
        TrackSegment segment = buildSegment();
        when(incidentRepository.findById(1L)).thenReturn(Optional.of(buildIncident(segment)));

        service.delete(1L);

        verify(incidentRepository, times(1)).deleteById(1L);
    }

    @Test
    void givenNonExistingId_whenDelete_thenThrowsResourceNotFoundException() {
        when(incidentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));
        verify(incidentRepository, never()).deleteById(any());
    }
}
