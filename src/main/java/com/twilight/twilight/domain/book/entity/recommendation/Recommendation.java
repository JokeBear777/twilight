package com.twilight.twilight.domain.book.entity.recommendation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommendation")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Recommendation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "AI_answer",
            nullable = false,
            columnDefinition = "LONGTEXT")
    @Lob
    private String aiAnswer;

}
