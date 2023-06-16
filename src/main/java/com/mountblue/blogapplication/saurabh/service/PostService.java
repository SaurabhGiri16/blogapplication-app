package com.mountblue.blogapplication.saurabh.service;

import com.mountblue.blogapplication.saurabh.model.User;
import com.mountblue.blogapplication.saurabh.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.mountblue.blogapplication.saurabh.repository.PostRepository;
import com.mountblue.blogapplication.saurabh.repository.TagRepository;
import com.mountblue.blogapplication.saurabh.model.Comment;
import com.mountblue.blogapplication.saurabh.model.Post;
import com.mountblue.blogapplication.saurabh.model.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserRepository userRepository;

    public Page<Post> getPosts(String order, String search, String authorId, String publishedAt, String tagId, Integer pageNumber,
                               Integer pageSize, Model model) {
        Page<Post> posts;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Tag> tags = this.tagRepository.findAll();
        List<Post> allPosts = this.postRepository.findAll();
        List<User> allUsers = this.userRepository.findAll();
        Map<Integer, String> tagNames = new HashMap<>();
        Map<Integer, String> authorNames = new HashMap<>();
        Map<Integer, LocalDate> dates = new HashMap<>();

        for (Tag tag : tags) {
            tagNames.put(tag.getId(), tag.getName());
        }

        for (Post post : allPosts) {
            if (!dates.containsValue(post.getPublishedAt())) {
                dates.put(post.getId(), post.getPublishedAt());
            }
        }

        for (User user : allUsers) {
            authorNames.put(user.getId(), user.getName());
        }

        model.addAttribute("authorNames", authorNames);
        model.addAttribute("dates", dates);
        model.addAttribute("tags", tagNames);

        if (order.equals("asc") || order.equals("desc")) {
            posts = getPostsInOrder(order, pageNumber, pageSize);
        } else if (!search.equals("src")) {
            posts = getPostsBySearch(search, pageNumber, 100);
        } else if (!(authorId.equals("0") && publishedAt.equals("0") && tagId.equals("0"))) {
            posts = filterPostsByAuthorIdPublishedAtAndTags(authorId, publishedAt, tagId, authorNames, dates, pageNumber, 100);
        } else {
            posts = this.postRepository.findAll(pageable);
        }
        return posts;
    }

    public Page<Post> getPostsInOrder(String order, Integer pageNumber, Integer pageSize) {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by("publishedAt").ascending());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by("publishedAt").descending());
        }
        Page<Post> page = this.postRepository.findAll(pageable);

        return page;
    }

    public Page<Post> getPostsBySearch(String search, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Post> page = this.postRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrTagsNameContainingIgnoreCase(search,
                        search, search, pageable);
        return page;
    }

    public Page<Post> filterPostsByAuthorIdPublishedAtAndTags(String authorId, String publishedAt, String tagId, Map<Integer, String> authorNames, Map<Integer, LocalDate> dates, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Object> authorFilters = new ArrayList<>();
        List<Object> publishedAtFilters = new ArrayList<>();
        List<Object> tagFilters = new ArrayList<>();
        String[] authors = authorId.split(",");
        String[] publishedAtByDate = publishedAt.split(",");
        String[] tags = tagId.split(",");

        for (String tag : tags) {
            tagFilters.add(Integer.parseInt(tag));
        }
        for (String author : authors) {
            authorFilters.add(authorNames.get(Integer.parseInt(author)));
        }
        for (String date : publishedAtByDate) {
            publishedAtFilters.add(dates.get(Integer.parseInt(date)));
        }
        return postRepository.findByAuthorInOrPublishedAtInOrTagsIn(authorFilters, publishedAtFilters, tagFilters,
                pageable);
    }

    public void publishPost(Post post, String tags) {
        String excerpt;
        if (post.getContent().length() > 120) {
            excerpt = post.getContent().substring(0, 120);
        } else {
            excerpt = post.getContent();
            excerpt += "....";
        }

        List<Tag> tagList = new ArrayList<>();
        String[] noOfTags = tags.split(",");
        for (String tag : noOfTags) {
            Tag addTag = new Tag();
            Tag existingTag = tagRepository.findByName(tag);
            if (existingTag == null) {
                addTag.setName(tag);
                tagList.add(addTag);
            } else {
                tagList.add(existingTag);
            }
        }
        post.setExcerpt(excerpt);
        post.setTags(tagList);
        postRepository.save(post);
    }

    public void publishUpdate(Integer id, String title, String content, String tags, String author) {
        Post post = postRepository.findById(id).get();
        List<Tag> tagList = new ArrayList<>();
        String[] noOfTags = tags.split(",");
        for (String tag : noOfTags) {
            Tag addTag = new Tag();
            Tag existingTag = tagRepository.findByName(tag);
            if (existingTag == null) {
                addTag.setName(tag);
                tagList.add(addTag);
            } else {
                tagList.add(existingTag);
            }
        }

        post.setId(id);
        post.setTitle(title);
        post.setAuthor(author);
        post.setContent(content);
        post.setUpdatedAt(LocalDateTime.now());
        post.setTags(tagList);
        postRepository.save(post);
    }

    public void getAllCommentsByPostId(Integer postId, Model model) {
        Post postOnPage = this.postRepository.findById(postId).get();
        List<Comment> allComments = postOnPage.getComments();
        model.addAttribute("comments", allComments);
        model.addAttribute("post", postOnPage);
    }

    public Post findPostByPostId(Integer postId, Model model) {
        Post post = this.postRepository.findById(postId).get();
        List<Tag> tags = post.getTags();
        String names = "";
        if (tags != null)
            for (Tag name : tags) {
                names += name.getName() + ",";
            }

        model.addAttribute("post", post);
        model.addAttribute("tag", names);
        return post;
    }

    public Boolean deletePostByPostId(int postId) {
        postRepository.deleteById(postId);
        return true;
    }

    public Post getPostByPostId(int postId) {
        return postRepository.findById(postId).get();
    }

    public Post savePost(Post post) {
        postRepository.save(post);
        return post;
    }

    public Post updatePost(int postId, Post post) {
        Post existPost = getPostByPostId(postId);
        List<Tag> tags = existPost.getTags();
        if (post.getTitle() != null) {
            existPost.setTitle(post.getTitle());
        }
        if (post.getAuthor() != null) {
            existPost.setAuthor(post.getAuthor());
        }
        if (post.getContent() != null) {
            existPost.setContent(post.getContent());
        }
        if (post.getExcerpt() != null) {
            existPost.setExcerpt(post.getExcerpt());
        }
        if (post.getTags() != null) {
            for (Tag tag : post.getTags()) {
                tags.add(tag);
            }
        }
        existPost.setTags(tags);
        existPost.setUpdatedAt(LocalDateTime.now());
        postRepository.save(existPost);
        return existPost;
    }

}
