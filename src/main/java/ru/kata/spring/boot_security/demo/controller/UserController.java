package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/{id}/userInfo")
    public String showUserInfo(@PathVariable Long id, @ModelAttribute("user") User user) {
        userServiceImpl.findUserById(id);
        return "userInfo";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable Long id, Model model, Principal principal) {
        String username = principal.getName();
        User user = userServiceImpl.findUserById(id);
        if (user == null || !user.getUsername().equals(username)) {
            return "redirect:/error";
        }
        model.addAttribute("user", user);
        return "edit";
    }
}