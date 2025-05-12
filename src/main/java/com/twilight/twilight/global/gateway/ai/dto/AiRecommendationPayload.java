package com.twilight.twilight.global.gateway.ai.dto;

import com.twilight.twilight.domain.member.entity.Personality;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiRecommendationPayload {
    private MemberInfo memberInfo;
    private List<BooksInfo> bookInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberInfo {
        private Long memberId;
        private Integer age;
        private String gender;
        private List<String> personalities;
        private List<String> interests;
        private List<QuestionAnswer> questionAnswers;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QuestionAnswer {
        private String question;
        private String userAnswer;
        private String matchingTag;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BooksInfo{
        private Long bookId;
        private String name;
        private String author;
        private Integer pageCount;
        private String description;
    }



}
