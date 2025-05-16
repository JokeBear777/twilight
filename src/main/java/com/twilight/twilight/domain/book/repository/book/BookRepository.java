package com.twilight.twilight.domain.book.repository.book;

import com.twilight.twilight.domain.book.entity.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    /* ---------- 1 개 태그 ---------- */

    @Query("""
           SELECT DISTINCT b
           FROM Book b
           JOIN BookTags bt ON bt.book = b
           WHERE bt.tag.tagId = :tagId
           """)
    List<Book> findBooksByTagId(@Param("tagId") Long tagId);

    @Query("SELECT COUNT(bt.book) FROM BookTags bt WHERE bt.tag.tagId = :tagId")
    Integer countBooksByTagId(@Param("tagId") Long tagId);


    /* ---------- 2 개 태그 ---------- */

    @Query(value = """
           SELECT COUNT(*) FROM (
               SELECT book_id
               FROM book_tags
               WHERE tag_id IN (:tagId1, :tagId2)
               GROUP BY book_id
               HAVING COUNT(DISTINCT tag_id) = 2
           ) AS matched_books
           """, nativeQuery = true)
    int countBooksByTwoTags(@Param("tagId1") Long tagId1,
                            @Param("tagId2") Long tagId2);

    @Query(value = """
           SELECT b.* 
           FROM book b
           WHERE b.book_id IN (
               SELECT bt.book_id
               FROM book_tags bt
               WHERE bt.tag_id IN (:tagId1, :tagId2)
               GROUP BY bt.book_id
               HAVING COUNT(DISTINCT bt.tag_id) = 2
           )
           """, nativeQuery = true)
    List<Book> findBooksByTwoTags(@Param("tagId1") Long tagId1,
                                  @Param("tagId2") Long tagId2);


    /* ---------- 3 개 태그 ---------- */

    @Query(value = """
           SELECT COUNT(*) FROM (
               SELECT book_id
               FROM book_tags
               WHERE tag_id IN (:tagId1, :tagId2, :tagId3)
               GROUP BY book_id
               HAVING COUNT(DISTINCT tag_id) = 3
           ) AS matched_books
           """, nativeQuery = true)
    int countBooksByThreeTags(@Param("tagId1") Long tagId1,
                              @Param("tagId2") Long tagId2,
                              @Param("tagId3") Long tagId3);

    @Query(value = """
           SELECT b.*
           FROM book b
           WHERE b.book_id IN (
               SELECT bt.book_id
               FROM book_tags bt
               WHERE bt.tag_id IN (:tagId1, :tagId2, :tagId3)
               GROUP BY bt.book_id
               HAVING COUNT(DISTINCT bt.tag_id) = 3
           )
           """, nativeQuery = true)
    List<Book> findBooksByThreeTags(@Param("tagId1") Long tagId1,
                                    @Param("tagId2") Long tagId2,
                                    @Param("tagId3") Long tagId3);


    /* ---------- 4 개 태그 ---------- */

    @Query(value = """
           SELECT COUNT(*) FROM (
               SELECT book_id
               FROM book_tags
               WHERE tag_id IN (:tagId1, :tagId2, :tagId3, :tagId4)
               GROUP BY book_id
               HAVING COUNT(DISTINCT tag_id) = 4
           ) AS matched_books
           """, nativeQuery = true)
    int countBooksByFourTags(@Param("tagId1") Long tagId1,
                             @Param("tagId2") Long tagId2,
                             @Param("tagId3") Long tagId3,
                             @Param("tagId4") Long tagId4);

    @Query(value = """
           SELECT b.*
           FROM book b
           WHERE b.book_id IN (
               SELECT bt.book_id
               FROM book_tags bt
               WHERE bt.tag_id IN (:tagId1, :tagId2, :tagId3, :tagId4)
               GROUP BY bt.book_id
               HAVING COUNT(DISTINCT bt.tag_id) = 4
           )
           """, nativeQuery = true)
    List<Book> findBooksByFourTags(@Param("tagId1") Long tagId1,
                                   @Param("tagId2") Long tagId2,
                                   @Param("tagId3") Long tagId3,
                                   @Param("tagId4") Long tagId4);


    /* ---------- 가변(목록) 태그 ---------- */

    @Query(value = """
           SELECT b.*
           FROM book b
           WHERE b.book_id IN (
               SELECT bt.book_id
               FROM book_tags bt
               WHERE bt.tag_id IN (:tagIds)
               GROUP BY bt.book_id
               HAVING COUNT(DISTINCT bt.tag_id) = :tagCount
           )
           """, nativeQuery = true)
    List<Book> findBooksByTags(@Param("tagIds") List<Long> tagIds,
                               @Param("tagCount") int tagCount);
}
