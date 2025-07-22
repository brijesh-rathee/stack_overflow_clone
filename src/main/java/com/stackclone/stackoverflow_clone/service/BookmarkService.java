package com.stackclone.stackoverflow_clone.service;

import com.stackclone.stackoverflow_clone.entity.Bookmark;
import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookmarkService {

    void toggleBookmark(User user, Question question);

    boolean isBookmarked(User user, Question question);

    List<Question> getBookmarkedQuestion(User user);

    Page<Question> getBookmarkedQuestions(User user, Pageable pageable);

}
