package com.twilight.twilight.domain.record.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseReadingRecordDto {
    private Long id;
    private String text;
    private LocalDateTime date;
}
