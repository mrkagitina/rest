package ru.kata.spring.boot_security.demo.service;



import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getAllUsers();
    void createUser(User user, List<Role> roles);
    void updateUser(User user);
    void deleteUser(Long id);
    User findUser(String username);
    User findUserById(Long id);
}
