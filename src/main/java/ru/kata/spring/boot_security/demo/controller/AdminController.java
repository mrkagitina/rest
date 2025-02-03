package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.dao.UserDaoImpl;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    PasswordEncoder passwordEncoder;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, PasswordEncoder passwordEncoder) {
        this.userServiceImpl = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/userList")
    public String listUsers(@ModelAttribute User user, Model model) {
        model.addAttribute("user", user);
        return "userList";
    }

    @GetMapping("/new")
    public String showAddUserForm() {
        return "addUser";
    }

    @PostMapping
    public String addUser(@ModelAttribute("user") User user) {
        passwordEncoder.encode(user.getPassword());
        userServiceImpl.createUser(user);
        return "redirect:/admin/userList";
    }

    @GetMapping("/{id}/editUser")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userServiceImpl.findUserById(id));
        return "editUser";
    }

    @PatchMapping("/{id}/update")
    public String update(@ModelAttribute("user") User user) {
        passwordEncoder.encode(user.getPassword());
        userServiceImpl.updateUser(user);
        return "redirect:/admin/userList";
    }

        @DeleteMapping("/{id}/delete")
        public String delete (@PathVariable("id") Long id){
            userServiceImpl.deleteUser(id);
            return "redirect:/admin/list";
        }
}
