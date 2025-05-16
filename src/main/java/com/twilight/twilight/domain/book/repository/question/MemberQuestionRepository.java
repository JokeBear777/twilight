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

    @Query(value = """
            SELECT *
            FROM member_question
            WHERE question_type = :type
              AND tag_id IS NOT NULL        -- ← null 제외
              AND tag_id = :tagId           -- ← 필요한 경우
            ORDER BY RAND()
            LIMIT 1
            """, nativeQuery = true)
    Optional<MemberQuestion> findRandomByQuestionTypeAndCategory(
            @Param("type") String type,  // enum 그대로 전달
            @Param("tagId") Long tagId                         // 파라미터 이름도 맞춰 변경
    );

    // answerId → tag_id 매핑용
    @Query("SELECT atm.tag.tagId FROM AnswerTagMapping atm WHERE atm.memberQuestionAnswer.memberQuestionAnswerId = :answerId")
    Long findTagIdByAnswerId(@Param("answerId") Long answerId);

    // 감성 3개 (랜덤)
    @Query(value = """
            SELECT * FROM member_question
            WHERE question_type = 'EMOTION'
              AND tag_id = :tagId
            ORDER BY RAND()
            LIMIT 3
            """, nativeQuery = true)
    List<MemberQuestion> findTop3EmotionByTag(@Param("tagId") Long tagId);

    // 자연어 1개
    @Query(value = """
            SELECT * FROM member_question
            WHERE question_type = 'NATURAL'
            ORDER BY RAND()
            LIMIT 1
            """, nativeQuery = true)
    Optional<MemberQuestion> findRandomNatural();

}
