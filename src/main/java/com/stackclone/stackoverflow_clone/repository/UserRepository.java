package com.stackclone.stackoverflow_clone.repository;

import com.stackclone.stackoverflow_clone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("SELECT u.username FROM User u")
    List<String> findAllUsernames();

    @Query("""
       SELECT u FROM User u
       WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))
          OR LOWER(COALESCE(u.location,'')) LIKE LOWER(CONCAT('%', :query, '%'))
      """)
    List<User> searchBasic(String query);
}
