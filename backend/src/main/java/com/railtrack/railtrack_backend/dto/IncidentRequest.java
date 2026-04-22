package com.railtrack.railtrack_backend.dto;

import com.railtrack.railtrack_backend.model.enums.IncidentSeverity;
import com.railtrack.railtrack_backend.model.enums.IncidentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IncidentRequest {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private IncidentSeverity severity;

    @NotNull
    private IncidentStatus status;

    @NotNull
    private Long trackSegmentId;
}
