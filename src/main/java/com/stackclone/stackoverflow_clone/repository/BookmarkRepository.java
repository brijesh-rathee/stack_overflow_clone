package com.stackclone.stackoverflow_clone.repository;

import com.stackclone.stackoverflow_clone.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
