package com.twilight.twilight.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "interest")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Interest {

    @Id
    @Column(name = "interest_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestId;

    @Column(name = "name", nullable = false)
    private String name;
}
