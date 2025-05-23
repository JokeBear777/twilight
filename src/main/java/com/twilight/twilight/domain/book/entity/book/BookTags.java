package com.twilight.twilight.domain.book.entity.book;

import com.twilight.twilight.domain.book.entity.tag.Tag;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
