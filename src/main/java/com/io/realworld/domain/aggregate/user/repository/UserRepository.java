package com.io.realworld.domain.aggregate.user.repository;

import com.io.realworld.domain.aggregate.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findAllByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);
    List<User> findAll();

    List<User> findAllByUsername(String username);
}
