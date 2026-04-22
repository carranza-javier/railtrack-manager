package com.railtrack.railtrack_backend.repository;

import com.railtrack.railtrack_backend.model.TrackSegment;
import com.railtrack.railtrack_backend.model.enums.TrackStatus;
import com.railtrack.railtrack_backend.model.enums.TrackType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TrackSegmentRepositoryTest {

    @Autowired
    private TrackSegmentRepository repository;

    private TrackSegment buildSegment(String name, String lineCode, TrackStatus status) {
        return TrackSegment.builder()
                .name(name)
                .lineCode(lineCode)
                .trackType(TrackType.MAIN)
                .status(status)
                .build();
    }

    @Test
    void givenSegmentsWithDifferentStatuses_whenFindByStatus_thenReturnsOnlyMatchingSegments() {
        repository.save(buildSegment("Seg A", "LC-01", TrackStatus.OPERATIONAL));
        repository.save(buildSegment("Seg B", "LC-02", TrackStatus.OPERATIONAL));
        repository.save(buildSegment("Seg C", "LC-03", TrackStatus.MAINTENANCE));

        List<TrackSegment> result = repository.findByStatus(TrackStatus.OPERATIONAL);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(s -> s.getStatus() == TrackStatus.OPERATIONAL));
    }

    @Test
    void givenNoSegmentsWithStatus_whenFindByStatus_thenReturnsEmptyList() {
        repository.save(buildSegment("Seg A", "LC-01", TrackStatus.OPERATIONAL));

        List<TrackSegment> result = repository.findByStatus(TrackStatus.CLOSED);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenSegmentsWithDifferentStatuses_whenCountByStatus_thenReturnsCorrectCount() {
        repository.save(buildSegment("Seg A", "LC-01", TrackStatus.MAINTENANCE));
        repository.save(buildSegment("Seg B", "LC-02", TrackStatus.MAINTENANCE));
        repository.save(buildSegment("Seg C", "LC-03", TrackStatus.OPERATIONAL));

        long count = repository.countByStatus(TrackStatus.MAINTENANCE);

        assertEquals(2L, count);
    }

    @Test
    void givenNoSegmentsWithStatus_whenCountByStatus_thenReturnsZero() {
        repository.save(buildSegment("Seg A", "LC-01", TrackStatus.OPERATIONAL));

        long count = repository.countByStatus(TrackStatus.CLOSED);

        assertEquals(0L, count);
    }

    @Test
    void givenSavedSegment_whenFindById_thenReturnsSegmentWithGeneratedTimestamps() {
        TrackSegment saved = repository.save(buildSegment("Seg A", "LC-01", TrackStatus.OPERATIONAL));

        TrackSegment found = repository.findById(saved.getId()).orElseThrow();

        assertNotNull(found.getCreatedAt());
        assertNotNull(found.getUpdatedAt());
        assertEquals("Seg A", found.getName());
    }
}
