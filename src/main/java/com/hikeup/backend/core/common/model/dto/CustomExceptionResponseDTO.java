package com.hikeup.backend.core.common.model.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Project: hikeup-backend
 *
 * @author : sojer
 * @since : 14.01.2024
 **/
public class CustomExceptionResponseDTO {
    
    private String message;
    private LocalDateTime occurredAt;

    public CustomExceptionResponseDTO() {
    }

    public CustomExceptionResponseDTO(String message, LocalDateTime occurredAt) {
        this.message = message;
        this.occurredAt = occurredAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }
}
