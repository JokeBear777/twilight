package com.twilight.twilight.domain.book.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookRecommendationRequestDto {
    private List<String>  questions;    // 4개
    private List<Long>    answerIds;   // index3 == null
    private List<String> answerTexts;  // index3 == textarea 값
    private Long tagId;
}
