package com.stackclone.stackoverflow_clone.repository;

import com.stackclone.stackoverflow_clone.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Optional<Badge> findByNameIgnoreCase(String name);
}
