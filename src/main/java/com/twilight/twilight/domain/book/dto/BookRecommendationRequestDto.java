package com.twilight.twilight.domain.book.dto;

import lombok.Data;

@Data
public class BookRecommendationRequestDto {
    private Long answer1Id;
    private Long answer2Id;
    private Long answer3Id;
    private Long answer4Id;
    private String answer5;
}
