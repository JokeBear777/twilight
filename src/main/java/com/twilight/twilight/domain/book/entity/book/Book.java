package com.twilight.twilight.domain.book.entity.book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Book {

    @Id
    @Column(name = "book_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "author")
    private String author;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
}
