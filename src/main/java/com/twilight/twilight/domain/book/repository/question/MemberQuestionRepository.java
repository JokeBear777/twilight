package com.twilight.twilight.domain.book.repository.question;

import com.twilight.twilight.domain.book.entity.question.MemberQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberQuestionRepository extends JpaRepository<MemberQuestion, Long> {
    List<MemberQuestion> findByMemberQuestionIdIn(List<Long> ids);
}
