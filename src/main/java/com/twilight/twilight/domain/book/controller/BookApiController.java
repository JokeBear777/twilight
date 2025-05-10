package com.twilight.twilight.domain.book.controller;

import com.twilight.twilight.domain.book.dto.BookRecommendationRequestDto;
import com.twilight.twilight.domain.book.dto.CompleteRecommendationDto;
import com.twilight.twilight.domain.book.dto.QuestionAnswerResponseDto;
import com.twilight.twilight.domain.book.service.BookService;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/book")   // 클래스 레벨 경로
@RequiredArgsConstructor
@Slf4j
public class BookApiController {

    private final BookService bookService;

    @GetMapping("/recommendation")
    public String recommendation(Model model) {
        List<QuestionAnswerResponseDto> questionAnswerResponseDtoList = bookService.createRandomQuestionAndAnswer();
        log.info("complete recommendation");
        model.addAttribute("questionAnswerList", questionAnswerResponseDtoList);
        return "book-recommendation";
    }

    @PostMapping("/recommendation")
    public String handleAnswer(
            @ModelAttribute BookRecommendationRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        bookService.requestRecommendation(request, userDetails);
        return "wating-recommendation";
    }

    @PostMapping("/recommendation/complete")
    public void completeRecommendation(
            @RequestBody CompleteRecommendationDto completeRecommendationDto,
            @RequestHeader("X-AI-AUTH-TOKEN") String token,
            Model model
    ) {
        bookService.completeRecommendation(completeRecommendationDto,token);
    }

    //pooling 방식으로 지속적인 요청
    @GetMapping("/recommendation/complete")
    public String getRecommendationComplete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model
    ) {

        return null;
    }
}
