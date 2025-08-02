package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Bookmark;
import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.repository.BookmarkRepository;
import com.stackclone.stackoverflow_clone.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    @Override
    public void toggleBookmark(User user, Question question) {
        Bookmark bookmark = bookmarkRepository.findByUserAndQuestion(user, question)
                .orElse(null);

        if (bookmark != null) {
            bookmarkRepository.delete(bookmark);
        } else {
            Bookmark newBookmark = Bookmark.builder()
                    .user(user)
                    .question(question)
                    .build();

            bookmarkRepository.save(newBookmark);
        }
    }

    @Override
    public boolean isBookmarked(User user, Question question) {
        return bookmarkRepository.findByUserAndQuestion(user, question).isPresent();
    }


    @Override
    public List<Question> getBookmarkedQuestion(User user) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user);
        List<Question> questions = new ArrayList<>();

        for (Bookmark bookmark : bookmarks) {
            questions.add(bookmark.getQuestion());
        }

        return questions;
    }

    @Override
    public Page<Question> getBookmarkedQuestions(User user, Pageable pageable) {

        return bookmarkRepository.findQuestionByUser(user, pageable);
    }
}
