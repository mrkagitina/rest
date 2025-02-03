package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/process_login")
    public String login(@ModelAttribute("user") User user) {
        return "/login";
    }

    @PostMapping("/select-role")
    public ModelAndView selectRole(@RequestParam Role role) {
        if ("ROLE_ADMIN".equals(role.getAuthority())) {
            return new ModelAndView("redirect:/admin/userList");
        } else if ("ROLE_USER".equals(role.getAuthority())) {
            return new ModelAndView("redirect:/user/userInfo");
        } else {
            return new ModelAndView("redirect:/error");
        }
    }

    @GetMapping("/register")
    public String registerPage(@ModelAttribute("user") User user) {
        return "register";
    }

    @PostMapping("/register")
    public String registration(@ModelAttribute("user") User user) {
        try {
            userService.createUser(user, user.getRoles());
        } catch (Exception e) {
            return "redirect:/error";
        }
        return "role";
    }
}
