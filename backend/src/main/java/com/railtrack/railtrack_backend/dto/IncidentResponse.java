package com.railtrack.railtrack_backend.dto;

import com.railtrack.railtrack_backend.model.enums.IncidentSeverity;
import com.railtrack.railtrack_backend.model.enums.IncidentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class IncidentResponse {

    private Long id;
    private String title;
    private String description;
    private IncidentSeverity severity;
    private IncidentStatus status;
    private LocalDateTime reportedAt;
    private LocalDateTime resolvedAt;
    private Long trackSegmentId;
    private String trackSegmentName;
}
