package com.mountblue.blogapplication.saurabh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mountblue.blogapplication.saurabh.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag findByName(String tag);
}
