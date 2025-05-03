package com.twilight.twilight.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_interests")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MemberInterests {

    @Id
    @Column(name = "member_interests_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberPersonalityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interests_id", nullable = false, updatable = false)
    private Interest interests;
}
