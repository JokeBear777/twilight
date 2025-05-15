package com.twilight.twilight.domain.record.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reading_record")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReadingRecord {

    @Id
    @Column(name = "reading_record_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long readingRecordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_record_id")
    private BookRecord bookRecord;

    @Column(name = "contents")
    private String contents;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
