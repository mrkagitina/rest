package ru.kata.spring.boot_security.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.dao.UserDaoImpl;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserDaoImpl userDaoImpl;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserDaoImpl userDaoImpl) {
        this.passwordEncoder = passwordEncoder;
        this.userDaoImpl = userDaoImpl;
    }

    @Override
    public List<User> getAllUsers() {
        return userDaoImpl.getAllUsers();
    }

    @Transactional
    @Override
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDaoImpl.createUser(user);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDaoImpl.updateUser(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userDaoImpl.deleteUser(id);
    }

    @Transactional
    @Override
    public User findUserById(Long id) {
        return userDaoImpl.findUserById(id);
    }

    @Transactional
    @Override
    public User findUserByUsername(String username) {
        return userDaoImpl.findUserByUsername(username);
    }
}