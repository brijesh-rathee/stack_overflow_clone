package com.stackclone.stackoverflow_clone.repository;

import com.stackclone.stackoverflow_clone.entity.Question;

import com.stackclone.stackoverflow_clone.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findByTagsContaining(Tag tag, Pageable pageable);

    @Query("""
            SELECT DISTINCT q FROM Question q
            LEFT JOIN q.tags t
            LEFT JOIN q.answers a
            WHERE LOWER(q.title) LIKE %:keyword%
               OR LOWER(t.name) LIKE %:keyword%
               OR LOWER(a.content) LIKE %:keyword%
            """)
    Page<Question> searchByTitleTagOrAnswer(String keyword, Pageable pageable);

    Page<Question> findByTagsId(Long tagId, Pageable pageable);

    Page<Question> findByVoteCountLessThan(int votes, PageRequest pageRequest);

}
