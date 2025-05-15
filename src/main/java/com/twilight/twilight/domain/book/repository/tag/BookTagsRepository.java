package com.twilight.twilight.domain.book.repository.tag;

import com.twilight.twilight.domain.book.entity.book.BookTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookTagsRepository extends JpaRepository<BookTags,Long> {

    @Query("SELECT bt FROM BookTags bt WHERE bt.book.bookId = :bookId AND bt.tag.tagType = com.twilight.twilight.domain.book.entity.tag.Tag.TagType.CATEGORY")
    Optional<BookTags> getBookCategoryByBookBookId(@Param("bookId") Long bookId);

}
