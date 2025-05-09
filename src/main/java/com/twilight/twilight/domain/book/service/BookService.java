package com.twilight.twilight.domain.book.service;

import com.twilight.twilight.domain.book.dto.BookRecommendationRequestDto;
import com.twilight.twilight.domain.book.dto.QuestionAnswerResponseDto;
import com.twilight.twilight.domain.book.entity.book.Book;
import com.twilight.twilight.domain.book.entity.book.BookTags;
import com.twilight.twilight.domain.book.entity.question.MemberQuestion;
import com.twilight.twilight.domain.book.entity.tag.Tag;
import com.twilight.twilight.domain.book.repository.book.BookRepository;
import com.twilight.twilight.domain.book.repository.question.AnswerTagMappingRepository;
import com.twilight.twilight.domain.book.repository.question.MemberQuestionAnswerRepository;
import com.twilight.twilight.domain.book.repository.question.MemberQuestionRepository;
import com.twilight.twilight.domain.member.entity.Member;
import com.twilight.twilight.domain.member.repository.MemberRepository;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import com.twilight.twilight.global.cache.MemberQuestionCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final int MIN = 5;
    private static final int MAX = 15;
    private static final int RANDOM_PICK = 10;

    private final MemberQuestionRepository memberQuestionRepository;
    private final MemberQuestionAnswerRepository memberQuestionAnswerRepository;
    private final BookRepository bookRepository;
    private final MemberQuestionCache memberQuestionCache;
    private final AnswerTagMappingRepository answerTagMappingRepository;
    private final MemberRepository memberRepository;

    public List<QuestionAnswerResponseDto> createRandomQuestionAndAnswer() {

        List<MemberQuestion> questions = new ArrayList<>();

        MemberQuestion categoryQuestion =
                memberQuestionRepository.findRandomByQuestionType(MemberQuestion.questionType.CATEGORY.name())
                        .orElseThrow(() -> new RuntimeException("CATEGORY 질문이 없습니다"));

        questions.add(categoryQuestion);

        MemberQuestion themeQuestion = memberQuestionRepository.findRandomByQuestionType(MemberQuestion.questionType.THEME.name())
                .orElseThrow(() -> new RuntimeException("THEME 질문이 없습니다"));

        questions.add(themeQuestion);

        for (int i = 0; i < 3; i++) {
            MemberQuestion emotionQuestion = memberQuestionRepository.findRandomByQuestionType(MemberQuestion.questionType.EMOTION.name())
                    .orElseThrow(() -> new RuntimeException("EMOTION 질문이 없습니다"));

            questions.add(emotionQuestion);
        }

        return questions.stream().map(q -> {
            List<QuestionAnswerResponseDto.AnswerDto> answers = memberQuestionAnswerRepository.findByMemberQuestion(q)
                    .stream()
                    .map(a -> new QuestionAnswerResponseDto.AnswerDto(a.getMemberQuestionAnswerId(), a.getAnswer()))
                    .collect(Collectors.toList());
            return new QuestionAnswerResponseDto(q.getQuestion(), answers);
        }).collect(Collectors.toList());
    }

    //태그 찾아서 검색 시스템 개발하자
    @Transactional(readOnly=true)
    public void requestRecommendation(
            BookRecommendationRequestDto request,
            CustomUserDetails userDetails
            ) {

        List<Tag> tagList = new ArrayList<>();
        tagList.add(findTagByAnswerId(request.getAnswer1Id()));
        tagList.add(findTagByAnswerId(request.getAnswer2Id()));
        tagList.add(findTagByAnswerId(request.getAnswer3Id()));
        tagList.add(findTagByAnswerId(request.getAnswer4Id()));


        //일정개수 이하로 추려지면 사용자 정보와 함께 ai서버로 보낸다, 남은 책의 개수가 5~15개 사이가 될때까지 필터링을 한다
        //대분류
        int byOneTagCount = bookRepository.countBooksByTagId(tagList.get(0).getTagId());
        if (byOneTagCount == 0) {
            throw new NoSuchElementException("대분류: " + tagList.get(0).getTagId() + "에 맞는 책이 없습니다");
        }
        if (byOneTagCount < 5 ) { //대분류만으로 분류가 끝나면(도서 5개 미만), 바로 서버로 보냄
            List<Book> bookList = bookRepository.findBooksByTagId(tagList.get(0).getTagId());
            sendInfoToAiServer(bookList, userDetails.getMember(), request);
            return;
        }

        //대 + 중 분류
        int byTwoTagsCount = bookRepository.countBooksByTwoTags(tagList.get(0).getTagId(), tagList.get(1).getTagId());

        if (5 <= byTwoTagsCount  && byTwoTagsCount <= 15) {
            List<Book> bookList = bookRepository.findBooksByTwoTags(tagList.get(0).getTagId(), tagList.get(1).getTagId());
            sendInfoToAiServer(bookList, userDetails.getMember(), request);
            return;
        }

        //대 + 중 + 감성태그 1개 분류
        int byThreeTagsCount = bookRepository.countBooksByThreeTags(
                tagList.get(0).getTagId(), tagList.get(1).getTagId(), tagList.get(2).getTagId());
        if (5 <= byThreeTagsCount  && byThreeTagsCount <= 15) {
            List<Book> bookList = bookRepository.findBooksByThreeTags(
                    tagList.get(0).getTagId(), tagList.get(1).getTagId(), tagList.get(2).getTagId());
            sendInfoToAiServer(bookList, userDetails.getMember(), request);
            return;
        }

        //대 + 중 + 다른 감성태그 1개 분류
        byThreeTagsCount = bookRepository.countBooksByThreeTags(
                tagList.get(0).getTagId(), tagList.get(1).getTagId(), tagList.get(3).getTagId());
        if (5 <= byThreeTagsCount  && byThreeTagsCount <= 15) {
            List<Book> bookList = bookRepository.findBooksByThreeTags(
                    tagList.get(0).getTagId(), tagList.get(1).getTagId(), tagList.get(3).getTagId());
            sendInfoToAiServer(bookList, userDetails.getMember(), request);
            return;
        }

        //대 + 중 + 감성태그 2개 분류
        int byFourTagsCount = bookRepository.countBooksByFourTags(
                tagList.get(0).getTagId(), tagList.get(1).getTagId(),
                tagList.get(2).getTagId(), tagList.get(3).getTagId());
        if (5 <= byFourTagsCount  && byFourTagsCount <= 15) {
            List<Book> bookList = bookRepository.findBooksByFourTags(
                    tagList.get(0).getTagId(), tagList.get(1).getTagId(),
                    tagList.get(2).getTagId(), tagList.get(3).getTagId());
            sendInfoToAiServer(bookList, userDetails.getMember(), request);
            return;
        }

        //이래도 분류가 안되면 4개 분류 + 랜덤
        Set<Integer> randomIndexSet = new HashSet<>();
        List<Book> bookList = bookRepository.findBooksByFourTags(
                tagList.get(0).getTagId(), tagList.get(1).getTagId(),
                tagList.get(2).getTagId(), tagList.get(3).getTagId());
        while (randomIndexSet.size() < 10) {
            Integer randomIndex = ThreadLocalRandom.current().nextInt(0, bookList.size());
            randomIndexSet.add(randomIndex);
        }

        List<Book> randomBookList = new ArrayList<>();
        randomIndexSet.forEach(randomIndex -> {
            randomBookList.add(bookList.get(randomIndex));
        });

        sendInfoToAiServer(randomBookList, userDetails.getMember(), request);
    }

    private Tag findTagByAnswerId(Long answerId) {
        return answerTagMappingRepository.findByMemberAnswer_MemberQuestionAnswerId(answerId)
                .orElseThrow(() -> new NoSuchElementException("해당 답변에 매핑된 태그가 없습니다."))
                .getTag();
    }

    private void sendInfoToAiServer(
            List<Book> bookList,
            Member member,
            BookRecommendationRequestDto request) {

    }



}
