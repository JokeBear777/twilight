package com.twilight.twilight.domain.book.entity.book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book_description")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BookDescription {

    @Id
    @Column(name = "book_description_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookDescriptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Lob
    @Column(name = "description")
    private String description;

}
