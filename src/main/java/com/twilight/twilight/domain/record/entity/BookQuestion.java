package com.twilight.twilight.domain.record.entity;

import com.twilight.twilight.domain.book.entity.tag.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book_question")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BookQuestion {

    @Id
    @Column(name = "book_question_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long book_question_Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Column(name = "question", nullable = false)
    private String question;
}
