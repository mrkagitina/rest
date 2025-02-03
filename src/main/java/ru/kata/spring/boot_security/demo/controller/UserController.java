package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userInfo")
    public String showUserInfo(@ModelAttribute("user") User user) {
        return "userInfo";
    }

    @PutMapping("/edit")
    public String showEditUserForm(@RequestParam("id") String useraname, Model model, Principal principal) {
        User user = userService.findUser(principal.getName());
        model.addAttribute("user", user);
        return "edit";
    }

    @PutMapping("/update")
    public String updateUser(@RequestParam("role") List<Role> roles, @ModelAttribute("user") User user) {
        userService.createUser(user, roles);
        return "redirect:/userInfo";
    }
}