package com.twilight.twilight.domain.book.entity.question;

import com.twilight.twilight.domain.book.entity.tag.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answer_tag_mapping")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AnswerTagMapping {

    @Id
    @Column(name = "answer_tag_mapping_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerTagMappingId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_question_answer_id")
    private MemberQuestionAnswer memberAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

}
