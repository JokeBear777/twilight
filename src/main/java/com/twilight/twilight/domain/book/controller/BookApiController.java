package com.twilight.twilight.domain.book.controller;

import com.twilight.twilight.domain.book.entity.recommendation.Recommendation;
import com.twilight.twilight.domain.book.service.BookService;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping("/book/api")   // 클래스 레벨 경로
@RequiredArgsConstructor
@Slf4j
public class BookApiController {

    private final BookService bookService;

    @GetMapping("/recommendation/complete")
    @ResponseBody
    public ResponseEntity<?> checkRecommendationComplete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        boolean exists = bookService.findRecommendationOnly(userDetails.getMember().getMemberId()).isPresent();
        return exists ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
    }
}
