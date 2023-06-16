package com.mountblue.blogapplication.saurabh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mountblue.blogapplication.saurabh.model.Post;
import com.mountblue.blogapplication.saurabh.service.PostService;

@Controller
public class HomeController {
    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String Posts(Model model, @RequestParam(value = "", required = false) String home,
                            @RequestParam(value = "sortField", defaultValue = "publishedAt", required = false) String sortField,
                            @RequestParam(value = "search", defaultValue = "src", required = false) String search,
                            @RequestParam(value = "order", defaultValue = "", required = false) String order,
                            @RequestParam(value = "authorId", defaultValue = "0", required = false) String authorId,
                            @RequestParam(value = "publishedAt", defaultValue = "0", required = false) String publishedAt,
                            @RequestParam(value = "tagId", defaultValue = "0", required = false) String tagId,
                            @RequestParam(value = "start", defaultValue = "0", required = false) Integer pageNumber,
                            @RequestParam(value = "limit", defaultValue = "10", required = false) Integer pageSize) {
        pageNumber /= 10;
        Page<Post> posts = postService.getPosts(order, search, authorId, publishedAt, tagId, pageNumber,
                pageSize, model);

        model.addAttribute("order", order);
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", posts.getTotalPages());
        return "home";
    }
}