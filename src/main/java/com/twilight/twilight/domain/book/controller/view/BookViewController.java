package com.twilight.twilight.domain.book.controller.view;

import com.twilight.twilight.domain.book.dto.QuestionAnswerResponseDto;
import com.twilight.twilight.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller("/book")
@RequiredArgsConstructor
public class BookViewController {

    private final BookService bookService;

    @GetMapping("/recommendation")
    public String recommendation(Model model) {
        List<QuestionAnswerResponseDto> questionAnswerResponseDtoList = bookService.createRandomQuestionAndAnswer();
        model.addAttribute("question-answer", questionAnswerResponseDtoList);
        return "book-recommendation";
    }

}
