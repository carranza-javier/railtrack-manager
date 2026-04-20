package com.railtrack.railtrack_backend.model;

import com.railtrack.railtrack_backend.model.enums.TrackStatus;
import com.railtrack.railtrack_backend.model.enums.TrackType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "track_segments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String lineCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrackType trackType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrackStatus status;

    private Double length;

    private LocalDate lastMaintenanceDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
