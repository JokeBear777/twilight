package com.twilight.twilight.domain.book.entity.question;

import com.twilight.twilight.domain.record.entity.MemberReadBooks;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_question_answer")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MemberQuestionAnswer {

    @Id
    @Column(name = "member_question_answer_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberQuestionAnswerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_question_id")
    private MemberQuestion memberQuestion;

    @Column(name = "answer")
    private String answer;

}
