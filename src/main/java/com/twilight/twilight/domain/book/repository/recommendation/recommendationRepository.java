package com.twilight.twilight.domain.book.repository.recommendation;

import com.twilight.twilight.domain.book.entity.recommendation.recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface recommendationRepository extends JpaRepository<recommendation, Long> {
}
