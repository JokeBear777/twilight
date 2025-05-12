package com.twilight.twilight.domain.book.dto;

import lombok.Data;

@Data
public class CompleteRecommendationDto {

    private Long memberId;

    private Long bookId;

    private String aiAnswer;

}
