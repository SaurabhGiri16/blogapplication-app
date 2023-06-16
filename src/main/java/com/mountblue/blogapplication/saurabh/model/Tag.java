package com.mountblue.blogapplication.saurabh.model;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Tag(int id, String name, LocalDateTime created_at, LocalDateTime updated_at) {
        super();
        this.id = id;
        this.name = name;
        this.createdAt = created_at;
        this.updatedAt = updated_at;
    }

    @ManyToMany
    private List<Post> posts = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Tag() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Tag [id=" + id + ", name=" + name + ", created_at=" + createdAt + ", updated_at=" + updatedAt + "]";
    }

}
