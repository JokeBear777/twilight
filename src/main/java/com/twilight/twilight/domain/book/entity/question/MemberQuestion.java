package com.twilight.twilight.domain.book.entity.question;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberQuestionId;

    @Column(name = "question", nullable = false)
    private String question;

}
