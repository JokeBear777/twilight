package com.twilight.twilight.domain.book.controller;

import com.twilight.twilight.domain.book.dto.BookRecommendationRequestDto;
import com.twilight.twilight.domain.book.dto.CompleteRecommendationDto;
import com.twilight.twilight.domain.book.dto.QuestionAnswerResponseDto;
import com.twilight.twilight.domain.book.dto.RecommendationViewDto;
import com.twilight.twilight.domain.book.entity.recommendation.Recommendation;
import com.twilight.twilight.domain.book.service.BookService;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/book")   // 클래스 레벨 경로
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;


    @GetMapping("/recommendation")
    public String recommendation(Model model) {
        QuestionAnswerResponseDto questionAnswerResponseDtoList = bookService.createCategoryQuestionAndAnswer();
        log.info("complete recommendation");
        model.addAttribute("category", questionAnswerResponseDtoList);
        return "category-recommendation";
    }

    @GetMapping("/recommendation/{tag_id}")
    public String recommendation(@PathVariable("tag_id") String tagId, Model model) {
        log.info("[TEST] tag_id = {} 받음, emotion 생성 준비", tagId);
        List<QuestionAnswerResponseDto> questionAnswerResponseDtoList
                = bookService.createRandomQuestionAndAnswerVer2(Long.parseLong(tagId));
        model.addAttribute("questionAnswerList", questionAnswerResponseDtoList);
        model.addAttribute("tagId", tagId);
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

    @PostMapping("/recommendation/complete/ai")
    public ResponseEntity<Void> completeRecommendation(
            @RequestBody CompleteRecommendationDto completeRecommendationDto,
            @RequestHeader("X-AI-AUTH-TOKEN") String token,
            Model model
    ) {
        log.info("AI 서버로부터 추천 결과 받음");
        bookService.completeRecommendation(completeRecommendationDto,token);
        return ResponseEntity.ok().build();
    }

    //pooling 방식으로 지속적인 요청
    /**
    @GetMapping("/recommendation/complete")
    public Object getRecommendationComplete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model
    ) {
        Optional<Recommendation> result =
                bookService.getRecommendationResult(userDetails.getMember().getMemberId());

        log.info("memberId = {} request recommendation pooling", userDetails.getMember().getMemberId());

        if (result.isPresent()) {
            RecommendationViewDto recommendationViewDto = bookService.getRecommendationViewDto(result.get().getBookId(), result.get().getAiAnswer());
            model.addAttribute("book", recommendationViewDto);
            return "recommendation-complete"; // 뷰 이름 반환
        }

        return ResponseEntity.noContent().build(); // 204 No Content
    }
            **/
    @GetMapping("/recommendation/complete")
    public String renderRecommendationPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Recommendation recommendation = bookService.findRecommendationOnly(userDetails.getMember().getMemberId())
                .orElseThrow(() -> new IllegalStateException("추천 결과 없음"));

        bookService.deleteRecommendation(userDetails.getMember().getMemberId()); // 여기서 삭제

        RecommendationViewDto viewDto = bookService.getRecommendationViewDto(
                recommendation.getBookId(),
                recommendation.getAiAnswer()
        );
        model.addAttribute("book", viewDto);
        return "recommendation-complete";
    }

}
