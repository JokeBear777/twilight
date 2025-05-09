package com.twilight.twilight.domain.book.repository.tag;

import com.twilight.twilight.domain.book.entity.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
