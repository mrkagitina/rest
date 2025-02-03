package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class DBInitialization {

    private final UserService userService;
    private final RoleService roleService; // Добавлен сервис для работы с ролями
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DBInitialization(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        Role adminRole = roleService.findByName("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleService.save(adminRole);
        }

        Role userRole = roleService.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            roleService.save(userRole);
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setAge(23);
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRoles(List.of(adminRole, userRole));
        userService.createUser(admin);

        User user = new User();
        user.setUsername("user");
        user.setAge(32);
        user.setPassword(passwordEncoder.encode("user"));
        user.setRoles(List.of(userRole));
        userService.createUser(user);
    }
}