package com.mountblue.blogapplication.saurabh.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String excerpt;
    @Column(columnDefinition = "varchar(3500)")
    private String content;
    private String author;
    private boolean is_published;
    private LocalDate publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    public LocalDate getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDate publishedAt) {
        this.publishedAt = publishedAt;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    public Post(int id, String title, String excerpt, String content, String author, boolean is_published,
                LocalDate published_at, LocalDateTime created_at, LocalDateTime updated_at) {
        super();
        this.id = id;
        this.title = title;
        this.excerpt = excerpt;
        this.content = content;
        this.author = author;
        this.is_published = is_published;
        this.publishedAt = published_at;
        this.createdAt = created_at;
        this.updatedAt = updated_at;
    }

    @PrePersist
    public void prePersist() {
        this.publishedAt = LocalDate.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.is_published = true;
    }

    public Post() {
        super();
        // TODO Auto-generated constructor stub
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isIs_published() {
        return is_published;
    }

    public void setIs_published(boolean is_published) {
        this.is_published = is_published;
    }

    public LocalDate getPublished_at() {
        return publishedAt;
    }

    public void setPublished_at(LocalDate published_at) {
        this.publishedAt = published_at;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created_at) {
        this.createdAt = created_at;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updated_at) {
        this.updatedAt = updated_at;
    }


    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Post [id=" + id + ", title=" + title + ", excerpt=" + excerpt + ", content=" + content + ", author="
                + author + ", is_published=" + is_published + ", published_at=" + publishedAt + ", created_at="
                + createdAt + ", updated_at=" + updatedAt + "]";
    }

}
