package com.twilight.twilight.domain.member.repository;

import com.twilight.twilight.domain.member.entity.MemberPersonality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberPersonalityRepository extends JpaRepository<MemberPersonality, Long> {

    Optional<MemberPersonality> findByMember_memberId(Long memberId);
}
