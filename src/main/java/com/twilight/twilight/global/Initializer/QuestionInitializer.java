package com.twilight.twilight.global.Initializer;
import com.twilight.twilight.domain.book.repository.question.MemberQuestionRepository;
import com.twilight.twilight.global.cache.MemberQuestionCache;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class QuestionInitializer {

    private final MemberQuestionRepository memberQuestionRepository;
    private final MemberQuestionCache memberQuestionCache;

    @PostConstruct
    public void init() {
        long count = memberQuestionRepository.count();
        if (count > Integer.MAX_VALUE) {
            throw new IllegalStateException("질문 개수가 int 범위를 초과했습니다.");
        }
        memberQuestionCache.setMemberQuestionCount((int)count);
        log.info("질문 개수 = {}", count);
    }

}
