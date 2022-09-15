package com.io.realworld.domain.aggregate.user.repository;

import com.io.realworld.domain.aggregate.user.entity.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Long> {
    User save(User user);
    User findByEmail(String email);

}
