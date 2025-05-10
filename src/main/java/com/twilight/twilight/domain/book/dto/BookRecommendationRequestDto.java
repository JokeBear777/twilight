package com.twilight.twilight.domain.book.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookRecommendationRequestDto {
    List<String>  questions;    // 5개
    List<Long>    answerIds;   // index4 == null
    List<String> answerTexts;  // index4 == textarea 값
}
