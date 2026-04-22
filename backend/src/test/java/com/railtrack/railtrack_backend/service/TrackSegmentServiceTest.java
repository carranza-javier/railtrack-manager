package com.railtrack.railtrack_backend.service;

import com.railtrack.railtrack_backend.dto.TrackSegmentRequest;
import com.railtrack.railtrack_backend.dto.TrackSegmentResponse;
import com.railtrack.railtrack_backend.exception.ResourceNotFoundException;
import com.railtrack.railtrack_backend.model.TrackSegment;
import com.railtrack.railtrack_backend.model.enums.TrackStatus;
import com.railtrack.railtrack_backend.model.enums.TrackType;
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
class TrackSegmentServiceTest {

    @Mock
    private TrackSegmentRepository repository;

    @InjectMocks
    private TrackSegmentService service;

    private TrackSegment buildSegment() {
        return TrackSegment.builder()
                .id(1L)
                .name("Segment A")
                .lineCode("LC-01")
                .trackType(TrackType.MAIN)
                .status(TrackStatus.OPERATIONAL)
                .length(12.5)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private TrackSegmentRequest buildDto() {
        TrackSegmentRequest dto = new TrackSegmentRequest();
        dto.setName("Segment A");
        dto.setLineCode("LC-01");
        dto.setTrackType(TrackType.MAIN);
        dto.setStatus(TrackStatus.OPERATIONAL);
        dto.setLength(12.5);
        return dto;
    }

    @Test
    void givenExistingSegments_whenFindAll_thenReturnsPageOfResponses() {
        Page<TrackSegment> page = new PageImpl<>(List.of(buildSegment()));
        when(repository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<TrackSegmentResponse> result = service.findAll(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Segment A", result.getContent().get(0).getName());
        assertEquals("LC-01", result.getContent().get(0).getLineCode());
    }

    @Test
    void givenExistingId_whenFindById_thenReturnsResponse() {
        when(repository.findById(1L)).thenReturn(Optional.of(buildSegment()));

        TrackSegmentResponse result = service.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Segment A", result.getName());
        assertEquals(TrackStatus.OPERATIONAL, result.getStatus());
    }

    @Test
    void givenNonExistingId_whenFindById_thenThrowsResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void givenValidDto_whenCreate_thenReturnsCreatedResponse() {
        when(repository.save(any(TrackSegment.class))).thenReturn(buildSegment());

        TrackSegmentResponse result = service.create(buildDto());

        assertEquals("Segment A", result.getName());
        assertEquals("LC-01", result.getLineCode());
        assertEquals(TrackType.MAIN, result.getTrackType());
        verify(repository, times(1)).save(any(TrackSegment.class));
    }

    @Test
    void givenExistingId_whenUpdate_thenReturnsUpdatedResponse() {
        TrackSegment updated = buildSegment();
        updated.setName("Updated Segment");

        when(repository.findById(1L)).thenReturn(Optional.of(buildSegment()));
        when(repository.save(any(TrackSegment.class))).thenReturn(updated);

        TrackSegmentRequest dto = buildDto();
        dto.setName("Updated Segment");

        TrackSegmentResponse result = service.update(1L, dto);

        assertEquals("Updated Segment", result.getName());
        verify(repository, times(1)).save(any(TrackSegment.class));
    }

    @Test
    void givenNonExistingId_whenUpdate_thenThrowsResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(99L, buildDto()));
        verify(repository, never()).save(any());
    }

    @Test
    void givenExistingId_whenDelete_thenDeleteByIdIsCalled() {
        when(repository.findById(1L)).thenReturn(Optional.of(buildSegment()));

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void givenNonExistingId_whenDelete_thenThrowsResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));
        verify(repository, never()).deleteById(any());
    }
}
