package com.railtrack.railtrack_backend.controller;

import com.railtrack.railtrack_backend.dto.TrackSegmentDto;
import com.railtrack.railtrack_backend.dto.TrackSegmentResponse;
import com.railtrack.railtrack_backend.service.TrackSegmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tracks")
@RequiredArgsConstructor
public class TrackSegmentController {

    private final TrackSegmentService service;

    @GetMapping
    public ResponseEntity<Page<TrackSegmentResponse>> getAll(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackSegmentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<TrackSegmentResponse> create(@Valid @RequestBody TrackSegmentDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrackSegmentResponse> update(@PathVariable Long id,
                                                       @Valid @RequestBody TrackSegmentDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
