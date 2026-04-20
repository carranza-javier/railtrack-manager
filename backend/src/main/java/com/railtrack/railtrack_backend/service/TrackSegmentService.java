package com.railtrack.railtrack_backend.service;

import com.railtrack.railtrack_backend.dto.TrackSegmentDto;
import com.railtrack.railtrack_backend.dto.TrackSegmentResponse;
import com.railtrack.railtrack_backend.exception.ResourceNotFoundException;
import com.railtrack.railtrack_backend.model.TrackSegment;
import com.railtrack.railtrack_backend.repository.TrackSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackSegmentService {

    private final TrackSegmentRepository repository;

    public List<TrackSegmentResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public TrackSegmentResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public TrackSegmentResponse create(TrackSegmentDto dto) {
        TrackSegment segment = TrackSegment.builder()
                .name(dto.getName())
                .lineCode(dto.getLineCode())
                .trackType(dto.getTrackType())
                .status(dto.getStatus())
                .length(dto.getLength())
                .lastMaintenanceDate(dto.getLastMaintenanceDate())
                .build();
        return toResponse(repository.save(segment));
    }

    public TrackSegmentResponse update(Long id, TrackSegmentDto dto) {
        TrackSegment segment = getOrThrow(id);
        segment.setName(dto.getName());
        segment.setLineCode(dto.getLineCode());
        segment.setTrackType(dto.getTrackType());
        segment.setStatus(dto.getStatus());
        segment.setLength(dto.getLength());
        segment.setLastMaintenanceDate(dto.getLastMaintenanceDate());
        return toResponse(repository.save(segment));
    }

    public void delete(Long id) {
        getOrThrow(id);
        repository.deleteById(id);
    }

    private TrackSegment getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TrackSegment", id));
    }

    private TrackSegmentResponse toResponse(TrackSegment segment) {
        return TrackSegmentResponse.builder()
                .id(segment.getId())
                .name(segment.getName())
                .lineCode(segment.getLineCode())
                .trackType(segment.getTrackType())
                .status(segment.getStatus())
                .length(segment.getLength())
                .lastMaintenanceDate(segment.getLastMaintenanceDate())
                .createdAt(segment.getCreatedAt())
                .updatedAt(segment.getUpdatedAt())
                .build();
    }
}
