package com.io.realworld.repository;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User save(User user);
    User findByEmail(String email);
}
