package com.mountblue.blogapplication.saurabh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.mountblue.blogapplication.saurabh.service.CommentService;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/addComment{postId}")
    public String addComment(@PathVariable("postId") int postId, @RequestParam("name") String name,
                             @RequestParam("email") String email, @RequestParam("comment") String comment) {
        commentService.addCommentByPostId(postId, name, email, comment);
        return "redirect:/post{postId}";
    }

    @PostMapping("/updateComment/{commentId}/{postId}")
    @PreAuthorize("authentication.name == @postService.getPostByPostId(#postId).getAuthor()")
    public String updateCommentByCommentId(@PathVariable("commentId") Integer commentId, @PathVariable("postId") Integer postId,
                                           @RequestParam("updatedComment") String updatedComment, Model model) {
        this.commentService.updateCommentByCommentId(commentId, updatedComment);
        return "redirect:/post{postId}";
    }

    @PostMapping("/deleteComment/{commentId}/{postId}")
    @PreAuthorize("authentication.name == @postService.getPostByPostId(#postId).getAuthor()")
    public String deleteCommentByCommentId(@PathVariable("commentId") Integer commentId, @PathVariable("postId") Integer postId, Model model) {
        this.commentService.deleteCommentByCommentId(commentId);
        return "redirect:/post{postId}";
    }
}
