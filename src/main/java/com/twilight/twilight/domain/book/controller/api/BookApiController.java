package com.twilight.twilight.domain.book.controller.api;

import com.twilight.twilight.domain.book.dto.BookRecommendationRequestDto;
import com.twilight.twilight.domain.book.service.BookService;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/book")   // 클래스 레벨 경로
@RequiredArgsConstructor
@Slf4j
public class BookApiController {

    private final BookService bookService;

    @PostMapping("/recommendation")
    public String handleAnswer(
            @ModelAttribute BookRecommendationRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        bookService.requestRecommendation(request, userDetails);
        return "wating-recommendation";
    };
}
