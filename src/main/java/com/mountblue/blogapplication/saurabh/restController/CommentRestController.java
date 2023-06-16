package com.mountblue.blogapplication.saurabh.restController;

import com.mountblue.blogapplication.saurabh.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.mountblue.blogapplication.saurabh.model.Comment;
import com.mountblue.blogapplication.saurabh.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/blog-app")
public class CommentRestController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getAllCommentsForPost(@PathVariable int postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable int commentId) {
        Comment comment = commentService.getCommentByCommentId(commentId);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> createCommentForPost(@PathVariable int postId, @RequestBody Comment comment) {
        comment.setPost(postService.getPostByPostId(postId));
        Comment createdComment = commentService.saveComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    @PreAuthorize("@securityServiceRest.isValidUserForPost(authentication, #postId)")
    public ResponseEntity<Comment> updateCommentByCommentId(@PathVariable int commentId, @RequestBody Comment comment, @PathVariable int postId) {
        Comment updatedComment = commentService.updateCommentByCommentId(commentId, comment);
        if (updatedComment != null) {
            return ResponseEntity.ok(updatedComment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    @PreAuthorize("@securityServiceRest.isValidUserForPost(authentication, #postId)")
    public ResponseEntity<String> deleteCommentByCommentId(@PathVariable int commentId, @PathVariable int postId) {
        boolean deleted = commentService.deleteCommentByCommentId(commentId);
        if (deleted) {
            return ResponseEntity.ok("Comment deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
