package com.stackclone.stackoverflow_clone.repository;

import com.stackclone.stackoverflow_clone.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByAnswerIdIn(List<Long> answerIds);

}
