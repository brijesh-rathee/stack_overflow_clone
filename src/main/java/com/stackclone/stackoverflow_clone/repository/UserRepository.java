package com.stackclone.stackoverflow_clone.repository;

import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);

    boolean existsByEmail(String email);

    Page<User> findByUsernameContainingIgnoreCase(String keyword, Pageable pageable);

    Page<User> findByRole(UserRole role, Pageable pageable);
}
