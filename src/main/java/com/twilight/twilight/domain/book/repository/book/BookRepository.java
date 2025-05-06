package com.twilight.twilight.domain.book.repository.book;

import com.twilight.twilight.domain.book.entity.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
