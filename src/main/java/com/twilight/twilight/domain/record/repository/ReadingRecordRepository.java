package com.twilight.twilight.domain.record.repository;

import com.twilight.twilight.domain.record.entity.ReadingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReadingRecordRepository extends JpaRepository<ReadingRecord, Long> {
    List<ReadingRecord> findByBookRecord_BookRecordId(Long recordId);
}
