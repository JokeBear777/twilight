package com.twilight.twilight.domain.member.service;

import com.twilight.twilight.domain.member.dto.AddMemberRequestDto;
import com.twilight.twilight.domain.member.entity.Member;
import com.twilight.twilight.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //personality와 interest는 아직 구현 안함
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
        if (dto.getInterests().size() != 3) {
            throw new IllegalArgumentException("취미는 3개 선택해야합니다.");
        }

        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 3. Member 엔티티로 변환
        Member member = Member.builder()
                .memberName(dto.getMemberName())
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .age(dto.getAge())
                .gender(dto.getGender())
                .build();

        memberRepository.save(member);
    }

}
