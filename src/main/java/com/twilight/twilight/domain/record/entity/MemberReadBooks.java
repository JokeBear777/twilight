package com.twilight.twilight.domain.record.entity;

import com.twilight.twilight.domain.book.entity.book.Book;
import com.twilight.twilight.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "member_read_books")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MemberReadBooks {

    @Id
    @Column(name = "member_read_books_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberReadBooksId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false, updatable = false)
    private Book book;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

}

