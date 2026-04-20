package com.railtrack.railtrack_backend.config;

import com.railtrack.railtrack_backend.model.Incident;
import com.railtrack.railtrack_backend.model.TrackSegment;
import com.railtrack.railtrack_backend.model.enums.*;
import com.railtrack.railtrack_backend.repository.IncidentRepository;
import com.railtrack.railtrack_backend.repository.TrackSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final TrackSegmentRepository trackSegmentRepository;
    private final IncidentRepository incidentRepository;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            TrackSegment bern = trackSegmentRepository.save(TrackSegment.builder()
                    .name("Bern - Olten")
                    .lineCode("IC1")
                    .trackType(TrackType.MAIN)
                    .status(TrackStatus.OPERATIONAL)
                    .length(44.2)
                    .lastMaintenanceDate(LocalDate.of(2025, 11, 10))
                    .build());

            TrackSegment lausanne = trackSegmentRepository.save(TrackSegment.builder()
                    .name("Lausanne - Yverdon")
                    .lineCode("RE4")
                    .trackType(TrackType.SECONDARY)
                    .status(TrackStatus.MAINTENANCE)
                    .length(38.7)
                    .lastMaintenanceDate(LocalDate.of(2025, 6, 22))
                    .build());

            TrackSegment zurich = trackSegmentRepository.save(TrackSegment.builder()
                    .name("Zürich Depot West")
                    .lineCode("DEP-ZRH")
                    .trackType(TrackType.DEPOT)
                    .status(TrackStatus.OPERATIONAL)
                    .length(3.1)
                    .lastMaintenanceDate(LocalDate.of(2026, 1, 15))
                    .build());

            incidentRepository.save(Incident.builder()
                    .title("Signal failure at km 12")
                    .description("Automatic signal system unresponsive between km 12 and km 14. Manual override active.")
                    .severity(IncidentSeverity.HIGH)
                    .status(IncidentStatus.IN_PROGRESS)
                    .trackSegment(bern)
                    .build());

            incidentRepository.save(Incident.builder()
                    .title("Track geometry deviation")
                    .description("Routine measurement detected gauge widening above tolerance threshold.")
                    .severity(IncidentSeverity.MEDIUM)
                    .status(IncidentStatus.OPEN)
                    .trackSegment(lausanne)
                    .build());

            incidentRepository.save(Incident.builder()
                    .title("Switch motor malfunction")
                    .description("Point machine on switch 7B failed during shunting operation. Temporarily locked in position.")
                    .severity(IncidentSeverity.CRITICAL)
                    .status(IncidentStatus.OPEN)
                    .trackSegment(zurich)
                    .build());
        };
    }
}
