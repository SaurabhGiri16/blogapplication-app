package com.mountblue.blogapplication.saurabh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mountblue.blogapplication.saurabh.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByPostId(Integer postId);

    String deleteById(int commentId);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c IN :commentList")
    boolean deleteAll(@Param("commentList") List<Comment> commentList);
}
