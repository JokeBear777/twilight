package com.twilight.twilight.domain.book.repository.tag;

import com.twilight.twilight.domain.book.entity.book.BookTags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookTagsRepository extends JpaRepository<BookTags,Long> {
}
