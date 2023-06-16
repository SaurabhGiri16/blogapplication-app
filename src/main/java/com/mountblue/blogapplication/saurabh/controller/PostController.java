package com.mountblue.blogapplication.saurabh.controller;

import com.mountblue.blogapplication.saurabh.model.Post;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.mountblue.blogapplication.saurabh.service.PostService;

@Controller
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/newPost")
    public static String newPost() {
        return "new-post";
    }

    @PostMapping("/publishPost")
    public String publishPost(@ModelAttribute Post post, @RequestParam("tag") String tags) {
        postService.publishPost(post, tags);
        return "new-post";
    }

    @GetMapping("/post{postId}")
    public String getPostById(Model model, @PathVariable("postId") Integer postId) {
        this.postService.getAllCommentsByPostId(postId, model);
        return "post";
    }

    @GetMapping("/updatePost{postId}")
    @PreAuthorize("hasAuthority('ADMIN') or authentication.name == @postService.getPostByPostId(#postId).getAuthor()")
    public String updatePost(@PathVariable("postId") Integer postId, Model model, Authentication authentication) {
        Post post = postService.findPostByPostId(postId, model);
        return "update";
    }

    @PostMapping("/updatePost{postId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public String updatePost(@PathVariable("postId") Integer postId, @RequestParam("title") String title,
                             @RequestParam("content") String content, @RequestParam("tag") String tags,
                             @RequestParam("author") String author) {
        postService.publishUpdate(postId, title, content, tags, author);
        return "redirect:/";
    }

    @GetMapping("/deletePost{postId}")
    @PreAuthorize("hasAuthority('ADMIN') or authentication.name == @postService.getPostByPostId(#postId).getAuthor()")
    public String deletePost(@PathVariable("postId") Integer postId) {
        this.postService.deletePostByPostId(postId);
        return "redirect:/";
    }
}
