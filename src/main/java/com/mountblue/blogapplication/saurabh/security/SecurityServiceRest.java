package com.mountblue.blogapplication.saurabh.security;

import com.mountblue.blogapplication.saurabh.model.Post;
import com.mountblue.blogapplication.saurabh.model.User;
import com.mountblue.blogapplication.saurabh.service.PostService;
import com.mountblue.blogapplication.saurabh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;


@Configuration
public class SecurityServiceRest {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    public boolean isValidUser(Authentication authentication, int userId) {
        String username = authentication.getName();
        User user = userService.getUserById(userId);
        return user != null && user.getEmail().equals(username);
    }

    public boolean isValidUserForPost(Authentication authentication, int postId) {
        Post post = postService.getPostByPostId(postId);
        return post != null && authentication.getName().equals(post.getAuthor());
    }

    public boolean isAuthorisedToPublishPost(Authentication authentication, Post post) {
        String author = post.getAuthor();
        return author != null && authentication.getName().equals(author);
    }
}
