package com.twilight.twilight.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendationViewDto {

    private String aiAnswer;

    private Long bookId;
    private String bookCover;
    private String title;
    private String author;
    private String publisher;
    private LocalDateTime pubData;
    private String genre;


}
