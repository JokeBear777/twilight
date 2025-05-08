package com.twilight.twilight.domain.book.dto;

import lombok.Data;

@Data
public class BookRecommendationRequest {
    private Long answer1;
    private Long answer2;
    private Long answer3;
    private Long answer4;
    private String answer5;
}
