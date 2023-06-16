package com.mountblue.blogapplication.saurabh.service;

import com.mountblue.blogapplication.saurabh.model.User;
import com.mountblue.blogapplication.saurabh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.*;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).get();
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }

    public Boolean save(User user, String confirmPassword, Model model) {
        if (user.getPassword().equals(confirmPassword)) {
            Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser.isEmpty()) {
                user.setRole("AUTHOR");
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
                return true;
            } else {
                model.addAttribute("invalidEmail", "User Already Exists !!");
                return false;
            }
        } else {
            model.addAttribute("invalidPassword", "Mismatch Confirm Password !!");
            return false;
        }
    }


    public String addUser(User user, String confirmPassword) {
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            if (user.getPassword().equals(confirmPassword)) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
                return null;
            } else {
                return "Passwords Not Matched";
            }
        } else {
            return "Email Already Used";
        }
    }

    public boolean updateUser(int userId, User updatedUser) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findById(userId).get());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int userId) {
        return userRepository.findById(userId).get();
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    public void deleteUserById(int userId) {
        userRepository.deleteById(userId);
    }


}

