package com.railtrack.railtrack_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String message,
        List<String> errors
) {
    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(LocalDateTime.now(), status, message, null);
    }

    public static ErrorResponse of(int status, String message, List<String> errors) {
        return new ErrorResponse(LocalDateTime.now(), status, message, errors);
    }
}
