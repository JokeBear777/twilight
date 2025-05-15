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
@Table(name = "book_record")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BookRecord {

    @Id
    @Column(name = "book_record_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookRecordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false, updatable = false)
    private Book book;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

}

