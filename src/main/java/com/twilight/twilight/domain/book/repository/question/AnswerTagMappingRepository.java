package com.twilight.twilight.domain.book.repository.question;

import com.twilight.twilight.domain.book.entity.question.AnswerTagMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnswerTagMappingRepository extends JpaRepository<AnswerTagMapping, Long> {

    Optional<AnswerTagMapping> findByMemberQuestionAnswer_MemberQuestionAnswerId(Long memberAnswerId);

    @Query("""
           SELECT atm.tag.tagId
           FROM AnswerTagMapping atm
           WHERE atm.memberQuestionAnswer.memberQuestionAnswerId = :answerId
           """)
    Long findTagIdByAnswerId(@Param("answerId") Long answerId);
}
