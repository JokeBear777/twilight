package com.twilight.twilight.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionAnswerResponseDto {

    private String question;

    private List<AnswerDto> answers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AnswerDto {
        private Long answerId;
        private String answer;
    }

    private String questionType;

}
