package com.twilight.twilight.domain.book.entity.tag;

import com.twilight.twilight.domain.book.entity.book.Book;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book_tags")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BookTags {

    @Id
    @Column(name = "book_tags_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookTagsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tagId;
}
