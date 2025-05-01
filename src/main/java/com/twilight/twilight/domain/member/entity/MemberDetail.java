package com.twilight.twilight.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_detail")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MemberDetail {

    @Id
    @Column(name = "member_detail_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberDetailId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender")
    private String gender;
}
