package com.twilight.twilight.domain.book.service;

import com.twilight.twilight.domain.book.dto.QuestionAnswerResponseDto;
import com.twilight.twilight.domain.book.entity.question.MemberQuestion;
import com.twilight.twilight.domain.book.entity.question.MemberQuestionAnswer;
import com.twilight.twilight.domain.book.repository.book.BookRepository;
import com.twilight.twilight.domain.book.repository.question.MemberQuestionAnswerRepository;
import com.twilight.twilight.domain.book.repository.question.MemberQuestionRepository;
import com.twilight.twilight.global.cache.MemberQuestionCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final MemberQuestionRepository memberQuestionRepository;
    private final MemberQuestionAnswerRepository memberQuestionAnswerRepository;
    private final BookRepository bookRepository;
    private final MemberQuestionCache memberQuestionCache;

    public List<QuestionAnswerResponseDto> createRandomQuestionAndAnswer() {
        long count = memberQuestionCache.getMemberQuestionCount();
        if (count <= 1) {
            throw new IllegalStateException("랜덤 질문 생성을 위한 질문 개수가 부족합니다.");
        }

        Set<Long> randSet = new HashSet<>();
        while (randSet.size() < 5) {
            randSet.add(ThreadLocalRandom.current().nextLong(1, count));
        }

        List<MemberQuestion> questions = memberQuestionRepository.findByMemberQuestionIdIn(new ArrayList<>(randSet));

        return questions.stream().map(q -> {
            List<String> answers = memberQuestionAnswerRepository.findByMemberQuestion(q)
                    .stream()
                    .map(MemberQuestionAnswer::getAnswer)
                    .collect(Collectors.toList());

            return new QuestionAnswerResponseDto(q.getQuestion(), answers);
        }).collect(Collectors.toList());
    }

}
