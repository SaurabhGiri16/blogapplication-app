package com.mountblue.blogapplication.saurabh.controller;

import com.mountblue.blogapplication.saurabh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mountblue.blogapplication.saurabh.model.User;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public static String register() {
        return "register";
    }

    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user, @RequestParam("confirmPassword") String confirmPassword,
                           Model model) {
        if (userService.save(user, confirmPassword, model)) {
            return "/login";
        }
        return "/register";
    }

    @GetMapping("/login")
    public static String login() {
        return "login";
    }

}
