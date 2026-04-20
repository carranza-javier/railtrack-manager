package com.railtrack.railtrack_backend.repository;

import com.railtrack.railtrack_backend.model.Incident;
import com.railtrack.railtrack_backend.model.enums.IncidentSeverity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findByTrackSegmentId(Long trackSegmentId);

    List<Incident> findBySeverity(IncidentSeverity severity);
}
