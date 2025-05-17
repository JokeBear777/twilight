package com.twilight.twilight.domain.record.service;

import com.twilight.twilight.domain.book.entity.book.Book;
import com.twilight.twilight.domain.book.repository.book.BookRepository;
import com.twilight.twilight.domain.member.entity.Member;
import com.twilight.twilight.domain.record.dto.AddReadingRecordDto;
import com.twilight.twilight.domain.record.dto.BookRecordResponseDto;
import com.twilight.twilight.domain.record.dto.ResponseReadingRecordDto;
import com.twilight.twilight.domain.record.entity.BookRecord;
import com.twilight.twilight.domain.record.entity.ReadingRecord;
import com.twilight.twilight.domain.record.repository.BookRecordRepository;
import com.twilight.twilight.domain.record.repository.ReadingRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecordService {

    private final BookRecordRepository bookRecordRepository;
    private final BookRepository bookRepository;
    private final ReadingRecordRepository readingRecordRepository;

    @Transactional
    public void saveBookRecord(Member member, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));

        bookRecordRepository.save(
                BookRecord.builder()
                        .member(member)
                        .book(book)
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public List<BookRecordResponseDto> getBookRecordResponseDto(Long memberId) {

        List<BookRecord> bookRecords = bookRecordRepository.findByMember_MemberId(memberId);


        return bookRecords.stream()
                .map(bookRecord -> {
                    Book book = bookRecord.getBook();
                    if (book == null) return null;

                    List<ReadingRecord> readingRecords =
                            readingRecordRepository.findByBookRecord_BookRecordId(bookRecord.getBookRecordId());


                    List<BookRecordResponseDto.ReadingRecord> logDtos = readingRecords.stream()
                            .map(r -> BookRecordResponseDto.ReadingRecord.builder()
                                    .id(r.getReadingRecordId())
                                    .date(r.getCreatedAt())
                                    .text(r.getContents())
                                    .build())
                            .toList();


                    return BookRecordResponseDto.builder()
                            .id(book.getBookId())
                            .title(book.getName())
                            .author(book.getAuthor())
                            .cover(book.getCoverImageUrl())
                            .logs(logDtos)
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();
    }


    @Transactional
    public ResponseReadingRecordDto saveReadingRecord(
            Member member,
            Long bookId,
            AddReadingRecordDto addReadingRecordDto
    ) {
       BookRecord bookRecord = bookRecordRepository.findByMember_MemberIdAndBook_BookId(member.getMemberId(), bookId)
               .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));

       List<Long> bookRecordBookIdList =
                bookRecordRepository.findByMember_MemberId(member.getMemberId())
                        .stream()
                        .map(record -> record.getBook().getBookId())
                        .toList();

       if (!bookRecordBookIdList.contains(bookId)) {
           throw new RuntimeException("해당 북에 접근권한이 없습니다 ID: " + bookId);
       }

        ReadingRecord saved = readingRecordRepository.save(
                ReadingRecord.builder()
                        .bookRecord(bookRecord)
                        .contents(addReadingRecordDto.getText())
                        .build()
        );

        return ResponseReadingRecordDto.builder()
                .id(saved.getReadingRecordId())
                .text(saved.getContents())
                .date(saved.getCreatedAt())
                .build();
    }


}
