package com.mountblue.blogapplication.saurabh.service;

import com.mountblue.blogapplication.saurabh.model.Post;
import com.mountblue.blogapplication.saurabh.model.Comment;
import com.mountblue.blogapplication.saurabh.repository.CommentRepository;
import com.mountblue.blogapplication.saurabh.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    public void addCommentByPostId(int postId, String name, String email, String comment) {
        Comment newComment = new Comment();
        newComment.setName(name);
        newComment.setComment(comment);
        newComment.setEmail(email);
        newComment.setPost(this.postRepository.findById(postId).get());
        commentRepository.save(newComment);
    }

    public void updateCommentByCommentId(Integer commentId, String updatedComment) {
        Comment comment = commentRepository.findById(commentId).get();
        comment.setId(commentId);
        comment.setComment(updatedComment);
        comment.setUpdated_at(LocalDateTime.now());
        commentRepository.save(comment);
    }

    public void deleteCommentByCommentId(Integer commentId) {
        this.commentRepository.deleteById(commentId);
    }


    public Comment saveComment(Comment comment) {
        commentRepository.save(comment);
        return comment;
    }

    public List<Comment> getCommentsByPostId(int postId) {
        Post postOnPage = this.postRepository.findById(postId).get();
        return postOnPage.getComments();
    }

    public Comment getCommentByCommentId(int commentId) {
        return commentRepository.findById(commentId).get();
    }

    public Boolean deleteCommentByCommentId(int commentId) {
        return commentRepository.deleteById(commentId) == null ? true : false;
    }
    public Comment updateCommentByCommentId(Integer commentId, Comment updatedComment) {
        Comment comment = commentRepository.findById(commentId).get();
        comment.setComment(updatedComment.getComment());
        commentRepository.save(comment);
        return comment;
    }
}
