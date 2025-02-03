package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
public class AuthController {
    PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, @ModelAttribute User user, Model model) {
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        if (user.getRoles().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getName()))) {
            return "redirect:/admin/userList";
        } else {
            return "redirect:/user/userInfo";
        }
    }

    @GetMapping("/register")
    public String registerPage(@ModelAttribute("user") User user) {
        return "register";
    }

    @PostMapping("/register")
    public String registration(@ModelAttribute("user") User user) {
        try {
            userService.createUser(user);
        } catch (Exception e) {
            return "redirect:/error";
        }
        return "redirect:/login";
    }
}
