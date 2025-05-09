package com.twilight.twilight.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_personality",
        uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "personality_id"}))
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MemberPersonality {

    @Id
    @Column(name = "member_personality_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberPersonalityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personality_id", nullable = false, updatable = false)
    private Personality personality;
}
