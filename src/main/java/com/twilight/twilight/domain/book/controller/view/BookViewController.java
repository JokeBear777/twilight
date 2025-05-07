package com.twilight.twilight.domain.book.controller.view;

import com.twilight.twilight.domain.book.dto.QuestionAnswerResponseDto;
import com.twilight.twilight.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

@Controller
@RequestMapping("/book")   // 클래스 레벨 경로
@RequiredArgsConstructor
@Slf4j
public class BookViewController {

    private final BookService bookService;

    @GetMapping("/recommendation")
    public String recommendation(Model model) {
        List<QuestionAnswerResponseDto> questionAnswerResponseDtoList = bookService.createRandomQuestionAndAnswer();
        log.info("complete recommendation");
        model.addAttribute("questionAnswerList", questionAnswerResponseDtoList);
        return "book-recommendation";
    }

}
