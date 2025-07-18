package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.entity.Bookmark;
import com.stackclone.stackoverflow_clone.entity.Question;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.repository.BookmarkRepository;
import com.stackclone.stackoverflow_clone.repository.QuestionRepository;
import com.stackclone.stackoverflow_clone.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final QuestionRepository questionRepository;

    public void bookmark(Long questionId, User user) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        boolean alreadyBookmarked = bookmarkRepository.existsByUserAndQuestion(user, question);

        if (!alreadyBookmarked) {
            Bookmark bookmark = new Bookmark();
            bookmark.setUser(user);
            bookmark.setQuestion(question);
            bookmarkRepository.save(bookmark);
        }
    }

    public void removeBookmark(Long questionId, User user) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        Optional<Bookmark> bookmarkOpt = bookmarkRepository.findByUserAndQuestion(user, question);
        if (bookmarkOpt.isPresent()) {
            bookmarkRepository.delete(bookmarkOpt.get());
        }
    }
}
