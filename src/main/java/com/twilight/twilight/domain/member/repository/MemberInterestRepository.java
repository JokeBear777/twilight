package com.twilight.twilight.domain.member.repository;

import com.twilight.twilight.domain.member.entity.MemberInterests;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberInterestRepository extends JpaRepository<MemberInterests, Long> {

    Optional<MemberInterests> findByMember_memberId(Long memberId);
}
