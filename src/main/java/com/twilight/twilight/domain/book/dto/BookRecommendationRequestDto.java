package com.twilight.twilight.domain.book.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookRecommendationRequestDto {
    List<String>  questions;    // 4개
    List<Long>    answerIds;   // index3 == null
    List<String> answerTexts;  // index3 == textarea 값
}
