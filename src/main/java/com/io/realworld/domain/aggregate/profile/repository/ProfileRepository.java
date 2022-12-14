package com.io.realworld.domain.aggregate.profile.repository;

import com.io.realworld.domain.aggregate.profile.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ProfileRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFolloweeIdAndFollowerId(Long followeeId, Long followerId);

    List<Follow> findByFollowerId(Long followeeId);

    List<Follow> findByFolloweeId(Long followerId);

}
