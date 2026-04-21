package com.railtrack.railtrack_backend.service;

import com.railtrack.railtrack_backend.dto.IncidentDto;
import com.railtrack.railtrack_backend.dto.IncidentResponse;
import com.railtrack.railtrack_backend.exception.ResourceNotFoundException;
import com.railtrack.railtrack_backend.model.Incident;
import com.railtrack.railtrack_backend.model.TrackSegment;
import com.railtrack.railtrack_backend.repository.IncidentRepository;
import com.railtrack.railtrack_backend.repository.TrackSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final TrackSegmentRepository trackSegmentRepository;

    public Page<IncidentResponse> findAll(Pageable pageable) {
        return incidentRepository.findAll(pageable).map(this::toResponse);
    }

    public IncidentResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public IncidentResponse create(IncidentDto dto) {
        TrackSegment segment = getSegmentOrThrow(dto.getTrackSegmentId());
        Incident incident = Incident.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .severity(dto.getSeverity())
                .status(dto.getStatus())
                .trackSegment(segment)
                .build();
        return toResponse(incidentRepository.save(incident));
    }

    public IncidentResponse update(Long id, IncidentDto dto) {
        Incident incident = getOrThrow(id);
        TrackSegment segment = getSegmentOrThrow(dto.getTrackSegmentId());
        incident.setTitle(dto.getTitle());
        incident.setDescription(dto.getDescription());
        incident.setSeverity(dto.getSeverity());
        incident.setStatus(dto.getStatus());
        incident.setTrackSegment(segment);
        return toResponse(incidentRepository.save(incident));
    }

    public void delete(Long id) {
        getOrThrow(id);
        incidentRepository.deleteById(id);
    }

    private Incident getOrThrow(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident", id));
    }

    private TrackSegment getSegmentOrThrow(Long id) {
        return trackSegmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TrackSegment", id));
    }

    IncidentResponse toResponse(Incident incident) {
        return IncidentResponse.builder()
                .id(incident.getId())
                .title(incident.getTitle())
                .description(incident.getDescription())
                .severity(incident.getSeverity())
                .status(incident.getStatus())
                .reportedAt(incident.getReportedAt())
                .resolvedAt(incident.getResolvedAt())
                .trackSegmentId(incident.getTrackSegment().getId())
                .trackSegmentName(incident.getTrackSegment().getName())
                .build();
    }
}
