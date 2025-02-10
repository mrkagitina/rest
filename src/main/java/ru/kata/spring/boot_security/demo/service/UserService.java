package ru.kata.spring.boot_security.demo.service;



import ru.kata.spring.boot_security.demo.DTO.UserDto;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User findByUsername(String username);

    User getById(Long id);

    List<User> allUsers();

    UserDto update(UserDto userDto);

    UserDto save(UserDto userDto);

    void delete(Long id);
}
