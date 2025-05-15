package com.twilight.twilight.domain.record.controller;

import com.twilight.twilight.domain.record.dto.AddReadingRecordDto;
import com.twilight.twilight.domain.record.dto.BookRecordResponseDto;
import com.twilight.twilight.domain.record.entity.BookRecord;
import com.twilight.twilight.domain.record.service.RecordService;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/record/api")   // 클래스 레벨 경로
@RequiredArgsConstructor
@Slf4j
public class RecordApiController {


    private final RecordService recordService;

    @GetMapping("/books")
    public ResponseEntity<List<BookRecordResponseDto>> getBookList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<BookRecordResponseDto> dto = recordService.getBookRecordResponseDto(userDetails.getMember().getMemberId());

        if (dto.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(dto); // 200 OK + JSON body
    }

    @PostMapping("/books/{bookId}/reading-record")
    public ResponseEntity<?> addReadingRecord(
            @PathVariable Long bookId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody AddReadingRecordDto addReadingRecordDto) {
        recordService.saveReadingRecord(userDetails.getMember(), bookId, addReadingRecordDto);

        return ResponseEntity.ok().build();
    }


}
