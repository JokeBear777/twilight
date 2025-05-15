package com.twilight.twilight.domain.record.controller;


import com.twilight.twilight.domain.book.entity.book.Book;
import com.twilight.twilight.domain.record.service.RecordService;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/record")   // 클래스 레벨 경로
@RequiredArgsConstructor
@Slf4j
public class RecordController {

    private final RecordService recordService;

    @PostMapping("/book/save")
    public String saveBook(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model,
            Long bookId
    ) {
        recordService.saveBookRecord(userDetails.getMember(), bookId);
        return "record";
    }

    @GetMapping()
    public String record(@AuthenticationPrincipal CustomUserDetails userDetails) {

        return "record";
    }


}
