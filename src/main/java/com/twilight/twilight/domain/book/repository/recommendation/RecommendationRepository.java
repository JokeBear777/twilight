package com.twilight.twilight.domain.book.repository.recommendation;

import com.twilight.twilight.domain.book.entity.recommendation.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    Optional<Recommendation> findByMemberId(Long memberId);
}
