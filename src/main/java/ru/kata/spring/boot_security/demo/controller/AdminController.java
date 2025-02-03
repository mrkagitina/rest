package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userList")
    public String listUsers(Model model, Principal principal) {
        User currentUser = userService.findUser(principal.getName());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", userService.getAllUsers());
        return "userList";
    }

    @GetMapping("/new")
    public String showAddUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "addUser";
    }

    @PostMapping
    public String addUser(@ModelAttribute("user") User user) {
        List<Role> roles = user.getRoles();
        userService.createUser(user, roles);
        return "redirect:/userList";
    }

    @PutMapping("/editUser")
    public String showEditUserForm(@RequestParam("username") String username, Model model, Principal principal) {
        User user = userService.findUser(principal.getName());
        model.addAttribute("user", user);
        return "editUser";
    }

    @PutMapping("/update")
    public String updateUser(@RequestParam("username") String username, @ModelAttribute("user") User user) {
        user.setUsername(username);
        List<Role> roles = user.getRoles();
        userService.createUser(user, roles);
        return "redirect:/userList";
    }

    @DeleteMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/userList";
    }
}
