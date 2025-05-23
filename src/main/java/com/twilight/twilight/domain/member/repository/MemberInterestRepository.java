package com.twilight.twilight.domain.member.repository;

import com.twilight.twilight.domain.member.entity.Member;
import com.twilight.twilight.domain.member.entity.MemberInterests;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberInterestRepository extends JpaRepository<MemberInterests, Long> {

    List<MemberInterests> findByMember_memberId(Long memberId);
    void deleteByMember(Member member);
}
