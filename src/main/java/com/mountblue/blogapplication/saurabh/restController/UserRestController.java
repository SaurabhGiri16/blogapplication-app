package com.mountblue.blogapplication.saurabh.restController;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.mountblue.blogapplication.saurabh.model.User;
import com.mountblue.blogapplication.saurabh.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/api/blog-app")
public class UserRestController {
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> getUser(@PathVariable int userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        Model model;
        String confirmPassword = user.getPassword();
        if (user.getName() == null || user.getName().isEmpty()) {
            return ResponseEntity.badRequest().body("name is required");
        } else if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("email id is required");
        } else if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("password is required");
        } else {
            String responseRegistration = userService.addUser(user, confirmPassword);
            if (responseRegistration != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseRegistration);
            }
            return new ResponseEntity<>("User Registered", HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteAllUser() {
        userService.deleteAllUsers();
        return ResponseEntity.ok("All users deleted successfully");
    }

    @DeleteMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUserById(@PathVariable int userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.ok("User deleted successfully");
    }


}


