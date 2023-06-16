package com.mountblue.blogapplication.saurabh.restController;

import com.mountblue.blogapplication.saurabh.model.Post;
import com.mountblue.blogapplication.saurabh.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog-app")
public class PostRestController {
    @Autowired
    private PostService postService;

    @GetMapping("/")
    public ResponseEntity showPosts(Model model, @RequestParam(value = "", required = false) String home,
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
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable int postId, Model model) {
        Post post = postService.findPostByPostId(postId, model);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/posts")
    @PreAuthorize("hasAuthority('ADMIN') or @securityServiceRest.isAuthorisedToPublishPost(authentication, #post)")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post createdPost = postService.savePost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @PutMapping("/posts/{postId}")
    @PreAuthorize("hasAuthority('ADMIN') or @securityServiceRest.isValidUserForPost(authentication, #postId)")
    public ResponseEntity<Post> updatePost(@PathVariable int postId, @RequestBody Post post) {
        Post updatedPost = postService.updatePost(postId, post);
        if (updatedPost != null) {
            return ResponseEntity.ok(updatedPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/posts/{postId}")
    @PreAuthorize("hasAuthority('ADMIN') or @securityServiceRest.isValidUserForPost(authentication, #postId)")
    public ResponseEntity<String> deletePostById(@PathVariable int postId) {
        boolean deleted = postService.deletePostByPostId(postId);
        if (deleted) {
            return ResponseEntity.ok("Post deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}