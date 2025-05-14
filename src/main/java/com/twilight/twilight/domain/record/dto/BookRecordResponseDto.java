package com.twilight.twilight.domain.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRecordResponseDto {
    private Long id;
    private String title;
    private String author;
    private String cover;
    private List<ReadingRecord> logs;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReadingRecord {
        private Long id;
        private LocalDateTime date;
        private String text;
    }

}
