package com.twilight.twilight.domain.record.repository;

import com.twilight.twilight.domain.record.entity.BookRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRecordRepository extends JpaRepository<BookRecord, Long> {
    List<BookRecord> findByMember_MemberId(Long bookId);
}
