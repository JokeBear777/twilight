package com.twilight.twilight.domain.book.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class CompleteRecommendationDto {

    @JsonProperty("member_id")
    private Long memberId;

    @JsonProperty("book_id")
    private Long bookId;

    @JsonProperty("AI_answer")
    @Lob
    private String aiAnswer;

}
