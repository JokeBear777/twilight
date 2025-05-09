package com.twilight.twilight.domain.book.repository.question;

import com.twilight.twilight.domain.book.entity.question.MemberQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberQuestionRepository extends JpaRepository<MemberQuestion, Long> {

    List<MemberQuestion> findByMemberQuestionIdIn(List<Long> ids);

    @Query(value = "SELECT * FROM member_question WHERE question_type = :type ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<MemberQuestion> findRandomByQuestionType(@Param("type") String type);

}
