package com.stackclone.stackoverflow_clone.repository;

import com.stackclone.stackoverflow_clone.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByNameIgnoreCase(String name);

    @Query("SELECT u.followedTags FROM User u WHERE u.id = :userId")
    List<Tag> findFollowedTagsByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT t FROM Tag t
        LEFT JOIN t.questions q
        WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        GROUP BY t
        ORDER BY COUNT(q) DESC
    """)
    Page<Tag> searchByKeywordOrderByPopularity(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
        SELECT t FROM Tag t
        WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY t.name ASC
    """)
    Page<Tag> searchByKeywordOrderByNameAsc(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
        SELECT t FROM Tag t
        LEFT JOIN t.questions q
        GROUP BY t
        ORDER BY COUNT(q) DESC
    """)
    Page<Tag> findAllOrderByPopularity(Pageable pageable);

    Page<Tag> findAllByOrderByNameAsc(Pageable pageable);
}
