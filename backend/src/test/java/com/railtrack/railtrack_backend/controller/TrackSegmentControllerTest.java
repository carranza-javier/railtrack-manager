package com.railtrack.railtrack_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.railtrack.railtrack_backend.dto.TrackSegmentDto;
import com.railtrack.railtrack_backend.dto.TrackSegmentResponse;
import com.railtrack.railtrack_backend.exception.GlobalExceptionHandler;
import com.railtrack.railtrack_backend.exception.ResourceNotFoundException;
import com.railtrack.railtrack_backend.model.enums.TrackStatus;
import com.railtrack.railtrack_backend.model.enums.TrackType;
import com.railtrack.railtrack_backend.service.TrackSegmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TrackSegmentControllerTest {

    @Mock
    private TrackSegmentService service;

    @InjectMocks
    private TrackSegmentController controller;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    private TrackSegmentResponse buildResponse() {
        return TrackSegmentResponse.builder()
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

    private TrackSegmentDto buildDto() {
        TrackSegmentDto dto = new TrackSegmentDto();
        dto.setName("Segment A");
        dto.setLineCode("LC-01");
        dto.setTrackType(TrackType.MAIN);
        dto.setStatus(TrackStatus.OPERATIONAL);
        dto.setLength(12.5);
        return dto;
    }

    @Test
    void givenExistingSegments_whenGetAll_thenReturns200WithPage() throws Exception {
        List<TrackSegmentResponse> content = List.of(buildResponse());
        when(service.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(content, PageRequest.of(0, 10), content.size()));

        mockMvc.perform(get("/api/tracks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Segment A"))
                .andExpect(jsonPath("$.content[0].lineCode").value("LC-01"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void givenExistingId_whenGetById_thenReturns200WithResponse() throws Exception {
        when(service.findById(1L)).thenReturn(buildResponse());

        mockMvc.perform(get("/api/tracks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Segment A"))
                .andExpect(jsonPath("$.status").value("OPERATIONAL"));
    }

    @Test
    void givenNonExistingId_whenGetById_thenReturns404() throws Exception {
        when(service.findById(99L)).thenThrow(new ResourceNotFoundException("TrackSegment", 99L));

        mockMvc.perform(get("/api/tracks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenValidDto_whenCreate_thenReturns201WithResponse() throws Exception {
        when(service.create(any(TrackSegmentDto.class))).thenReturn(buildResponse());

        mockMvc.perform(post("/api/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildDto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Segment A"));
    }

    @Test
    void givenInvalidDto_whenCreate_thenReturns400() throws Exception {
        TrackSegmentDto invalid = new TrackSegmentDto();

        mockMvc.perform(post("/api/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenValidDto_whenUpdate_thenReturns200WithUpdatedResponse() throws Exception {
        TrackSegmentResponse updated = buildResponse();
        updated.setName("Updated Segment");
        when(service.update(eq(1L), any(TrackSegmentDto.class))).thenReturn(updated);

        TrackSegmentDto dto = buildDto();
        dto.setName("Updated Segment");

        mockMvc.perform(put("/api/tracks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Segment"));
    }

    @Test
    void givenNonExistingId_whenUpdate_thenReturns404() throws Exception {
        when(service.update(eq(99L), any(TrackSegmentDto.class)))
                .thenThrow(new ResourceNotFoundException("TrackSegment", 99L));

        mockMvc.perform(put("/api/tracks/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildDto())))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenExistingId_whenDelete_thenReturns204() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/tracks/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(1L);
    }

    @Test
    void givenNonExistingId_whenDelete_thenReturns404() throws Exception {
        doThrow(new ResourceNotFoundException("TrackSegment", 99L)).when(service).delete(99L);

        mockMvc.perform(delete("/api/tracks/99"))
                .andExpect(status().isNotFound());
    }
}
