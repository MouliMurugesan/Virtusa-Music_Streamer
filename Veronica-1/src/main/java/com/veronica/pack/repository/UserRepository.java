package com.veronica.pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.veronica.pack.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
 User findByEmailid(String email);
}
