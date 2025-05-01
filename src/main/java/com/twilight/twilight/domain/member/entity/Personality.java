package com.twilight.twilight.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "personality")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Personality {

    @Id
    @Column(name = "personality_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personalityId;

    @Column(name = "name", nullable = false)
    private String name;

}
