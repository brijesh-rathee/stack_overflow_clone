package com.stackclone.stackoverflow_clone.repository;

import com.stackclone.stackoverflow_clone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
