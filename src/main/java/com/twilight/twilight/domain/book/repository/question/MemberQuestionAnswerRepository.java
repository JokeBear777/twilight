package com.twilight.twilight.domain.book.repository.question;

import com.twilight.twilight.domain.book.entity.question.MemberQuestion;
import com.twilight.twilight.domain.book.entity.question.MemberQuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberQuestionAnswerRepository extends JpaRepository<MemberQuestionAnswer, Long> {
    List<MemberQuestionAnswer> findByMemberQuestion(MemberQuestion memberQuestion);
}
