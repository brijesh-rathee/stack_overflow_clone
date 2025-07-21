package com.stackclone.stackoverflow_clone.repository;

import com.stackclone.stackoverflow_clone.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    boolean existsByName(String tagName);
}
