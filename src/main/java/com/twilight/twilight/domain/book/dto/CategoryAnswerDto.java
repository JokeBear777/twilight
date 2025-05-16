package com.twilight.twilight.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryAnswerDto {

    private Long  answerId;  // member_question_answer_id
    private String answer;   // 답변 문구
    private Long  tagId;     // 이 답변에 매핑된 카테고리 Tag PK
}
