package com.mountblue.blogapplication.saurabh.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mountblue.blogapplication.saurabh.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrTagsNameContainingIgnoreCase(String title,
                                                                                                         String author, String tags, Pageable pageable);

    Page<Post> findByAuthorInOrPublishedAtInOrTagsIn(List<Object> authorFilters, List<Object> publishedAtFilters,
                                                     List<Object> tagFilters, Pageable pageable);
}
