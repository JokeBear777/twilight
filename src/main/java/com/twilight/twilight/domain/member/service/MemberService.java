package com.twilight.twilight.domain.member.service;

import com.twilight.twilight.domain.member.dto.AddMemberRequestDto;
import com.twilight.twilight.domain.member.dto.MyPageMemberInfoDto;
import com.twilight.twilight.domain.member.dto.MyPageUpdateDto;
import com.twilight.twilight.domain.member.entity.*;
import com.twilight.twilight.domain.member.repository.*;
import com.twilight.twilight.domain.member.type.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberPersonalityRepository memberPersonalityRepository;
    private final MemberInterestRepository memberInterestRepository;
    private final PersonalityRepository personalityRepository;
    private final InterestRepository interestRepository;

    @Transactional
    public void signup(AddMemberRequestDto dto) {
        if (dto.getMemberName() == null || dto.getMemberName().trim().isEmpty()) {
            throw new IllegalArgumentException("이름은 비어 있을 수 없습니다.");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("이메일은 비어 있을 수 없습니다.");
        }
        if (!dto.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
        if (dto.getPassword() == null || dto.getPassword().length() < 6) {
            throw new IllegalArgumentException("비밀번호는 6자 이상이어야 합니다.");
        }
        if (dto.getAge() == null) {
            throw new IllegalArgumentException("나이는 비어 있을 수 없습니다.");
        }
        if (dto.getGender() == null || dto.getGender().trim().isEmpty()) {
            throw new IllegalArgumentException("성별은 비어 있을 수 없습니다.");
        }

        if (dto.getPersonalities().size() != 3) {
            throw new IllegalArgumentException("성격은 3개 선택해야합니다.");
        }
        if (new HashSet<>(dto.getPersonalities()).size() != 3) {
            throw new IllegalArgumentException("성격은 중복 없이 3개 선택해야 합니다.");
        }

        if (dto.getInterests().size() != 3) {
            throw new IllegalArgumentException("취미는 3개 선택해야합니다.");
        }
        if (new HashSet<>(dto.getInterests()).size() != 3) {
            throw new IllegalArgumentException("취미는 중복 없이 3개 선택해야 합니다.");
        }

        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Member member = Member.builder()
                .memberName(dto.getMemberName())
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .role(Role.ROLE_USER.name())
                .age(dto.getAge())
                .gender(dto.getGender())
                .build();

        memberRepository.save(member);

        List<Personality> personalities = personalityRepository.findByNameIn(dto.getPersonalities());
        List<Interest> interests = interestRepository.findByNameIn(dto.getInterests());

        for (Personality personality : personalities) {
            MemberPersonality memberPersonality = MemberPersonality.builder()
                    .member(member)
                    .personality(personality)
                    .build();
            memberPersonalityRepository.save(memberPersonality);
        }

        for (Interest interest : interests) {
            MemberInterests memberInterests = MemberInterests.builder()
                    .member(member)
                    .interest(interest)
                    .build();
            memberInterestRepository.save(memberInterests);
        }
    }

    @Transactional(readOnly = true)
    public MyPageMemberInfoDto getMyPageMemberInfo(Member member) {

        List<String> memberPersonalityList
                = memberPersonalityRepository.findByMember_memberId(member.getMemberId())
                .stream()
                .map((personality)-> personality.getPersonality().getName())
                .toList();

        List<String> memberInterestsList
                = memberInterestRepository.findByMember_memberId(member.getMemberId())
                .stream()
                .map((interest) -> interest.getInterest().getName())
                .toList();

        return MyPageMemberInfoDto.builder()
                .id(member.getMemberId())
                .age(member.getAge())
                .gender(member.getGender())
                .memberPersonalityList(memberPersonalityList)
                .memberInterestList(memberInterestsList)
                .build();
    }

    @Transactional
    public void updateMemberInfo(Member member, MyPageUpdateDto dto) {

        member.setGender(dto.getGender());
        member.setAge(dto.getAge());

        memberRepository.save(member);

        // 기존 관심사/성격 삭제
        memberPersonalityRepository.deleteByMember(member);
        memberInterestRepository.deleteByMember(member);

        memberPersonalityRepository.flush();
        memberInterestRepository.flush();

        log.info("DTO personalities = {}", dto.getPersonalities());   // 문자열 리스트


        // 새 성격 추가
        List<Personality> personalities = personalityRepository.findByNameIn(dto.getPersonalities());
        for (Personality personality : personalities) {
            MemberPersonality mp = MemberPersonality.builder()
                    .member(member)
                    .personality(personality)
                    .build();
            memberPersonalityRepository.save(mp);
        }

        // 새 관심사 추가
        List<Interest> interests = interestRepository.findByNameIn(dto.getInterests());
        for (Interest interest : interests) {
            MemberInterests mi = MemberInterests.builder()
                    .member(member)
                    .interest(interest)
                    .build();
            memberInterestRepository.save(mi);
        }
    }


}
