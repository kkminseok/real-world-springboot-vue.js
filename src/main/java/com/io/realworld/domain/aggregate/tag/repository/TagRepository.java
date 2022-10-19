package com.io.realworld.domain.aggregate.tag.repository;

import com.io.realworld.domain.aggregate.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TagRepository extends JpaRepository<Tag,Long> {
}
