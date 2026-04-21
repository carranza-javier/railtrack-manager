package com.railtrack.railtrack_backend.repository;

import com.railtrack.railtrack_backend.model.TrackSegment;
import com.railtrack.railtrack_backend.model.enums.TrackStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackSegmentRepository extends JpaRepository<TrackSegment, Long> {

    List<TrackSegment> findByStatus(TrackStatus status);

    long countByStatus(TrackStatus status);
}
