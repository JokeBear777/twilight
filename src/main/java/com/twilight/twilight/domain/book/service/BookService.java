package com.twilight.twilight.domain.book.service;

import com.twilight.twilight.domain.book.dto.BookRecommendationRequestDto;
import com.twilight.twilight.domain.book.dto.CompleteRecommendationDto;
import com.twilight.twilight.domain.book.dto.QuestionAnswerResponseDto;
import com.twilight.twilight.domain.book.dto.RecommendationViewDto;
import com.twilight.twilight.domain.book.entity.book.Book;
import com.twilight.twilight.domain.book.entity.book.BookTags;
import com.twilight.twilight.domain.book.entity.question.MemberQuestion;
import com.twilight.twilight.domain.book.entity.question.MemberQuestionAnswer;
import com.twilight.twilight.domain.book.entity.recommendation.Recommendation;
import com.twilight.twilight.domain.book.entity.tag.Tag;
import com.twilight.twilight.domain.book.repository.book.BookRepository;
import com.twilight.twilight.domain.book.repository.question.AnswerTagMappingRepository;
import com.twilight.twilight.domain.book.repository.question.MemberQuestionAnswerRepository;
import com.twilight.twilight.domain.book.repository.question.MemberQuestionRepository;
import com.twilight.twilight.domain.book.repository.recommendation.RecommendationRepository;
import com.twilight.twilight.domain.book.repository.tag.BookTagsRepository;
import com.twilight.twilight.domain.member.entity.*;
import com.twilight.twilight.domain.member.repository.MemberInterestRepository;
import com.twilight.twilight.domain.member.repository.MemberPersonalityRepository;
import com.twilight.twilight.domain.member.repository.MemberRepository;
import com.twilight.twilight.domain.member.repository.PersonalityRepository;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import com.twilight.twilight.global.cache.MemberQuestionCache;
import com.twilight.twilight.global.gateway.ai.AiGateway;
import com.twilight.twilight.global.gateway.ai.dto.AiRecommendationPayload;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private static final int MIN = 5;
    private static final int MAX = 15;
    private static final int RANDOM_PICK = 10;
    private static final int TAG_ANSWER_COUNT = 5;

    private final MemberQuestionRepository memberQuestionRepository;
    private final MemberQuestionAnswerRepository memberQuestionAnswerRepository;
    private final BookRepository bookRepository;
    private final MemberQuestionCache memberQuestionCache;
    private final AnswerTagMappingRepository answerTagMappingRepository;
    private final MemberRepository memberRepository;
    private final PersonalityRepository personalityRepository;
    private final MemberPersonalityRepository memberPersonalityRepository;
    private final MemberInterestRepository memberInterestRepository;
    private final AiGateway aiGateway;
    private final RecommendationRepository recommendationRepository;
    private final BookTagsRepository bookTagsRepository;

    public List<QuestionAnswerResponseDto> createRandomQuestionAndAnswer() {

        Set<Long> selectedIds = new HashSet<>();
        List<MemberQuestion> questions = new ArrayList<>();

        MemberQuestion categoryQuestion =
                memberQuestionRepository.findRandomByQuestionType(MemberQuestion.questionType.CATEGORY.name())
                        .orElseThrow(() -> new RuntimeException("CATEGORY 질문이 없습니다"));

        questions.add(categoryQuestion);

        while (questions.size() < 3) {
            MemberQuestion emotionQuestion = memberQuestionRepository
                    .findRandomByQuestionType(MemberQuestion.questionType.EMOTION.name())
                    .orElseThrow(() -> new RuntimeException("EMOTION 질문이 없습니다"));

            if (!selectedIds.contains(emotionQuestion.getMemberQuestionId())) {
                selectedIds.add(emotionQuestion.getMemberQuestionId());
                questions.add(emotionQuestion);
            }
        }

        MemberQuestion themeQuestion = memberQuestionRepository.findRandomByQuestionType(MemberQuestion.questionType.NATURAL.name())
                .orElseThrow(() -> new RuntimeException("NATURAL 질문이 없습니다"));

        questions.add(themeQuestion);

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
        for (int i = 0; i < TAG_ANSWER_COUNT; i++) {
            tagList.add(findTagByAnswerId(request.getAnswerIds().get(i)));
        }

        //일정개수 이하로 추려지면 사용자 정보와 함께 ai서버로 보낸다, 남은 책의 개수가 5~15개 사이가 될때까지 필터링을 한다
        //대분류
        int byOneTagCount = bookRepository.countBooksByTagId(tagList.get(0).getTagId());
        if (byOneTagCount == 0) {
            throw new NoSuchElementException("대분류: " + tagList.get(0).getTagId() + "에 맞는 책이 없습니다");
        }
        if (byOneTagCount < 5 ) { //대분류만으로 분류가 끝나면(도서 5개 미만), 바로 서버로 보냄
            List<Book> bookListByCategory = bookRepository.findBooksByTagId(tagList.get(0).getTagId());
            sendInfoToAiServer(bookListByCategory, userDetails.getMember(), request, tagList);
            return;
        }

        //대 + 감성 A
        int byTwoTagsCount = bookRepository.countBooksByTwoTags(tagList.get(0).getTagId(), tagList.get(1).getTagId());
        if (5 <= byTwoTagsCount  && byTwoTagsCount <= 15) {
            List<Book> bookListByA = bookRepository.findBooksByTwoTags(tagList.get(0).getTagId(), tagList.get(1).getTagId());
            sendInfoToAiServer(bookListByA, userDetails.getMember(), request, tagList);
            return;
        }
        if (byTwoTagsCount < 5 ) {
            // 대+감성A
            List<Book> booksA = bookRepository.findBooksByTwoTags(tagList.get(0).getTagId(), tagList.get(1).getTagId());

            // 대+감성B
            List<Book> booksB = bookRepository.findBooksByTwoTags(tagList.get(0).getTagId(), tagList.get(2).getTagId());
            Set<Book> combinedBooks = new HashSet<>();
            combinedBooks.addAll(booksA);
            combinedBooks.addAll(booksB);

            if (combinedBooks.size() < 5) {
                // 대+감성C
                List<Book> booksC = bookRepository.findBooksByTwoTags(tagList.get(0).getTagId(), tagList.get(3).getTagId());
                combinedBooks.addAll(booksC);
            }

            // 랜덤으로 보내거나 전체 다 보냄
            List<Book> finalBookList = selectRandomBookList(new ArrayList<>(combinedBooks));
            sendInfoToAiServer(finalBookList, userDetails.getMember(), request, tagList);
            return;
        }

        //대 + 감성 A B
        int byThreeTagsCount = bookRepository.countBooksByThreeTags(
                tagList.get(0).getTagId(), tagList.get(1).getTagId(), tagList.get(2).getTagId());
        if (5 <= byThreeTagsCount  && byThreeTagsCount <= 15) {
            List<Book> bookListByAB = bookRepository.findBooksByThreeTags(
                    tagList.get(0).getTagId(), tagList.get(1).getTagId(), tagList.get(2).getTagId());
            sendInfoToAiServer(bookListByAB, userDetails.getMember(), request, tagList);
            return;
        }
        if (byThreeTagsCount < 5 ) {
            List<Book> bookListByA =
                    bookRepository.findBooksByTwoTags(tagList.get(0).getTagId(), tagList.get(1).getTagId());
            sendInfoToAiServer(selectRandomBookList(bookListByA), userDetails.getMember(), request, tagList);
        }

        //대 + 감성 A C
        byThreeTagsCount = bookRepository.countBooksByThreeTags(
                tagList.get(0).getTagId(), tagList.get(1).getTagId(), tagList.get(3).getTagId());
        if (5 <= byThreeTagsCount  && byThreeTagsCount <= 15) {
            List<Book> bookListByAC = bookRepository.findBooksByThreeTags(
                    tagList.get(0).getTagId(), tagList.get(1).getTagId(), tagList.get(3).getTagId());
            sendInfoToAiServer(bookListByAC, userDetails.getMember(), request, tagList);
            return;
        }
        if(byThreeTagsCount < 5 ) {
            List<Book> bookListByAB = bookRepository.findBooksByThreeTags(
                    tagList.get(0).getTagId(), tagList.get(1).getTagId(), tagList.get(2).getTagId());
            sendInfoToAiServer(
                    selectRandomBookList(bookListByAB), userDetails.getMember(), request, tagList);
        }

        //대 + 감성 B C
        byThreeTagsCount = bookRepository.countBooksByThreeTags(
                tagList.get(0).getTagId(), tagList.get(2).getTagId(), tagList.get(3).getTagId());
        if (5 <= byThreeTagsCount  && byThreeTagsCount <= 15) {
            List<Book> bookListByBC = bookRepository.findBooksByThreeTags(
                    tagList.get(0).getTagId(), tagList.get(2).getTagId(), tagList.get(3).getTagId());
            sendInfoToAiServer(bookListByBC, userDetails.getMember(), request, tagList);
            return;
        }
        if(byThreeTagsCount < 5 ) {
            List<Book> bookListByAC = bookRepository.findBooksByThreeTags(
                    tagList.get(0).getTagId(), tagList.get(1).getTagId(), tagList.get(3).getTagId());
            sendInfoToAiServer(
                    selectRandomBookList(bookListByAC), userDetails.getMember(), request, tagList);
        }


        //대 + 감성 ABC
        int byFourTagsCount = bookRepository.countBooksByFourTags(
                tagList.get(0).getTagId(), tagList.get(1).getTagId(),
                tagList.get(2).getTagId(), tagList.get(3).getTagId());
        if (5 <= byFourTagsCount  && byFourTagsCount <= 15) {
            List<Book> bookListByABC = bookRepository.findBooksByFourTags(
                    tagList.get(0).getTagId(), tagList.get(1).getTagId(),
                    tagList.get(2).getTagId(), tagList.get(3).getTagId());
            sendInfoToAiServer(bookListByABC, userDetails.getMember(), request, tagList);
            return;
        }
        if(byFourTagsCount < 5 ) {
            List<Book> bookListByBC = bookRepository.findBooksByThreeTags(
                    tagList.get(0).getTagId(), tagList.get(2).getTagId(), tagList.get(3).getTagId());
            sendInfoToAiServer(selectRandomBookList(
                    selectRandomBookList(bookListByBC)), userDetails.getMember(), request, tagList);
        }


        //이래도 분류가 안되면 4개 분류 + 랜덤
        Set<Integer> randomIndexSet = new HashSet<>();
        List<Book> bookList = bookRepository.findBooksByFourTags(
                tagList.get(0).getTagId(), tagList.get(1).getTagId(),
                tagList.get(2).getTagId(), tagList.get(3).getTagId());
        List<Book> randomBookList = selectRandomBookList(bookList);
        sendInfoToAiServer(randomBookList, userDetails.getMember(), request, tagList);
    }

    private List<Book> selectRandomBookList(List<Book> bookList) {
        int pickSize = Math.min(10, bookList.size());

        Set<Integer> randomIndexSet = new HashSet<>();
        while (randomIndexSet.size() < pickSize) {
            int randomIndex = ThreadLocalRandom.current().nextInt(bookList.size());
            randomIndexSet.add(randomIndex);
        }

        List<Book> randomBookList = new ArrayList<>();
        for (Integer index : randomIndexSet) {
            randomBookList.add(bookList.get(index));
        }

        return randomBookList;
    }

    private Tag findTagByAnswerId(Long answerId) {
        return answerTagMappingRepository.findByMemberAnswer_MemberQuestionAnswerId(answerId)
                .orElseThrow(() -> new NoSuchElementException("해당 답변에 매핑된 태그가 없습니다."))
                .getTag();
    }

    private void sendInfoToAiServer(
            List<Book> bookList,
            Member member,
            BookRecommendationRequestDto request,
            List<Tag> tagList) {

        AiRecommendationPayload.MemberInfo memberInfo = mapMemberToPayload(member, request, tagList);
        List<AiRecommendationPayload.BooksInfo> booksInfoList = mapBookListToPayload(bookList);
        AiRecommendationPayload aiRecommendationPayload = AiRecommendationPayload.builder()
                .memberInfo(memberInfo)
                .bookInfo(booksInfoList)
                .build();

        aiGateway.send(aiRecommendationPayload);
    }

    private AiRecommendationPayload.MemberInfo mapMemberToPayload (
            Member member,
            BookRecommendationRequestDto request,
            List<Tag> tagList
            ) {
        List<String> personalityStringList =
                memberPersonalityRepository.findByMember_memberId(member.getMemberId())
                        .stream()
                        .map(MemberPersonality::getPersonality)
                        .filter(Objects::nonNull)                     // getPersonality() null 방어
                        .map(Personality::getName)
                        .filter(Objects::nonNull)                     // getName() null 방어
                        .toList();

        List<String> interestStringList =
                memberInterestRepository.findByMember_memberId(member.getMemberId())
                        .stream()
                        .map(MemberInterests::getInterest)
                        .filter(Objects::nonNull)                     // getPersonality() null 방어
                        .map(Interest::getName)
                        .filter(Objects::nonNull)                     // getName() null 방어
                        .toList();

        List<AiRecommendationPayload.QuestionAnswer> questionAnswersList = new ArrayList<>();

        for (int i = 0; i < request.getQuestions().size(); i++) {

            String matchingTag = (i < tagList.size()) ? tagList.get(i).getName()
                    : null;   // step5는 null

            questionAnswersList.add(
                    AiRecommendationPayload.QuestionAnswer.builder()
                            .question(request.getQuestions().get(i))
                            .userAnswer(request.getAnswerTexts().get(i))
                            .matchingTag(matchingTag)
                            .build()
            );
        }

        return AiRecommendationPayload.MemberInfo.builder()
                .memberId(member.getMemberId())
                .age(member.getAge())
                .gender(member.getGender())
                .personalities(personalityStringList)
                .interests(interestStringList)
                .questionAnswers(questionAnswersList)
                .build();
    }

    private List<AiRecommendationPayload.BooksInfo> mapBookListToPayload(List<Book> bookList) {
        return  bookList.stream()
                .map(
                        (book) -> AiRecommendationPayload.BooksInfo.builder()
                                .bookId(book.getBookId())
                                .name(book.getName())
                                .author(book.getAuthor())
                                .pageCount(book.getPageCount())
                                .description(book.getDescription())
                                .build()
                )
                .toList();
    }

    public void completeRecommendation(
            CompleteRecommendationDto completeRecommendationDto,
            String token) {
        if (! token.equals(System.getenv("AI_SECRET_TOKEN"))) {
            log.info("식별되지 않은 ai 서버 접근");
            return;
        }

        recommendationRepository.save(
                Recommendation.builder()
                        .memberId(completeRecommendationDto.getMemberId())
                        .bookId(completeRecommendationDto.getBookId())
                        .aiAnswer(completeRecommendationDto.getAiAnswer())
                        .build()
        );
    }

    @Transactional
    public Optional<Recommendation> getRecommendationResult(Long memberId) {
        Optional<Recommendation> recommendationOptional = recommendationRepository.findById(memberId);
        recommendationOptional.ifPresent(recommendationRepository::delete);
        return recommendationOptional;
    }

    @Transactional(readOnly = true)
    public Book getBookInfo(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(()-> new RuntimeException("해당하는 책이 없습니다."));
    }

    @Transactional(readOnly = true)
    public String getBookCategory(Long bookId) {
        BookTags bookTag = bookTagsRepository.getBookCategoryByBookBookId(bookId)
                .orElseThrow(() ->new RuntimeException("책의 카테고리를 찾을 수 없습니다."));

        return bookTag.getTag().getName();
    }

    @Transactional(readOnly = true)
    public RecommendationViewDto getRecommendationViewDto(
            Long bookId,
            String aiAnswer
    ){
        Book bookResult =
                getBookInfo(bookId);
        String bookCategory = getBookCategory(bookId);

        return RecommendationViewDto.builder()
                .aiAnswer(aiAnswer)
                .bookId(bookResult.getBookId())
                .bookCover(bookResult.getCoverImageUrl())
                .title(bookResult.getName())
                .author(bookResult.getAuthor())
                .publisher(bookResult.getPublisher())
                .pubData(bookResult.getPublishedAt())
                .genre(bookCategory)
                .build();
    }


}
