package com.railtrack.railtrack_backend.dto;

import com.railtrack.railtrack_backend.model.enums.TrackStatus;
import com.railtrack.railtrack_backend.model.enums.TrackType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TrackSegmentRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String lineCode;

    @NotNull
    private TrackType trackType;

    @NotNull
    private TrackStatus status;

    private Double length;

    private LocalDate lastMaintenanceDate;
}
