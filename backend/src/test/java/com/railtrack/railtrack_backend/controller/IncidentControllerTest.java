package com.railtrack.railtrack_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.railtrack.railtrack_backend.dto.IncidentRequest;
import com.railtrack.railtrack_backend.dto.IncidentResponse;
import com.railtrack.railtrack_backend.exception.GlobalExceptionHandler;
import com.railtrack.railtrack_backend.exception.ResourceNotFoundException;
import com.railtrack.railtrack_backend.model.enums.IncidentSeverity;
import com.railtrack.railtrack_backend.model.enums.IncidentStatus;
import com.railtrack.railtrack_backend.service.IncidentService;
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
class IncidentControllerTest {

    @Mock
    private IncidentService service;

    @InjectMocks
    private IncidentController controller;

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

    private IncidentResponse buildResponse() {
        return IncidentResponse.builder()
                .id(1L)
                .title("Signal failure")
                .description("Red signal malfunction")
                .severity(IncidentSeverity.HIGH)
                .status(IncidentStatus.OPEN)
                .reportedAt(LocalDateTime.now())
                .trackSegmentId(1L)
                .trackSegmentName("Segment A")
                .build();
    }

    private IncidentRequest buildDto() {
        IncidentRequest dto = new IncidentRequest();
        dto.setTitle("Signal failure");
        dto.setDescription("Red signal malfunction");
        dto.setSeverity(IncidentSeverity.HIGH);
        dto.setStatus(IncidentStatus.OPEN);
        dto.setTrackSegmentId(1L);
        return dto;
    }

    @Test
    void givenExistingIncidents_whenGetAll_thenReturns200WithPage() throws Exception {
        List<IncidentResponse> content = List.of(buildResponse());
        when(service.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(content, PageRequest.of(0, 10), content.size()));

        mockMvc.perform(get("/api/incidents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Signal failure"))
                .andExpect(jsonPath("$.content[0].severity").value("HIGH"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void givenExistingId_whenGetById_thenReturns200WithResponse() throws Exception {
        when(service.findById(1L)).thenReturn(buildResponse());

        mockMvc.perform(get("/api/incidents/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Signal failure"))
                .andExpect(jsonPath("$.trackSegmentName").value("Segment A"));
    }

    @Test
    void givenNonExistingId_whenGetById_thenReturns404() throws Exception {
        when(service.findById(99L)).thenThrow(new ResourceNotFoundException("Incident", 99L));

        mockMvc.perform(get("/api/incidents/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenValidDto_whenCreate_thenReturns201WithResponse() throws Exception {
        when(service.create(any(IncidentRequest.class))).thenReturn(buildResponse());

        mockMvc.perform(post("/api/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildDto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Signal failure"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void givenInvalidDto_whenCreate_thenReturns400() throws Exception {
        IncidentRequest invalid = new IncidentRequest();

        mockMvc.perform(post("/api/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenValidDto_whenUpdate_thenReturns200WithUpdatedResponse() throws Exception {
        IncidentResponse updated = buildResponse();
        updated.setTitle("Track switch broken");
        when(service.update(eq(1L), any(IncidentRequest.class))).thenReturn(updated);

        IncidentRequest dto = buildDto();
        dto.setTitle("Track switch broken");

        mockMvc.perform(put("/api/incidents/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Track switch broken"));
    }

    @Test
    void givenNonExistingId_whenUpdate_thenReturns404() throws Exception {
        when(service.update(eq(99L), any(IncidentRequest.class)))
                .thenThrow(new ResourceNotFoundException("Incident", 99L));

        mockMvc.perform(put("/api/incidents/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildDto())))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenExistingId_whenDelete_thenReturns204() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/incidents/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(1L);
    }

    @Test
    void givenNonExistingId_whenDelete_thenReturns404() throws Exception {
        doThrow(new ResourceNotFoundException("Incident", 99L)).when(service).delete(99L);

        mockMvc.perform(delete("/api/incidents/99"))
                .andExpect(status().isNotFound());
    }
}
