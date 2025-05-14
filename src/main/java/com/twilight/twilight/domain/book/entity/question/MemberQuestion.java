package com.twilight.twilight.domain.book.entity.question;

import com.twilight.twilight.domain.book.entity.tag.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_question")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MemberQuestion {

    @Id
    @Column(name = "member_question_id", nullable = false)
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberQuestionId;

    @Column(name = "question", nullable = false)
    private String question;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private MemberQuestion.questionType questionType;

    public enum questionType {
        CATEGORY, // 대분류
        //THEME,    // 중분류 -> 삭제
        EMOTION,   // 감성 태그 질문
        NATURAL //자연어 질문
    }

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = true)
    private Tag tag;

}
