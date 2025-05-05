package com.twilight.twilight.domain.member.repository;

import com.twilight.twilight.domain.member.entity.Personality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonalityRepository extends JpaRepository<Personality, Long> {
    List<Personality> findByNameIn(List<String> names);
}
