package ru.kata.spring.boot_security.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> userFromDb = Optional.ofNullable(userRepository.findByUsername(username));
        return userFromDb.orElseThrow(() -> new EntityNotFoundException("Пользователь с именем " + username + " не найден"));
    }

    @Override
    public User getById(Long id) {
        Optional<User> userFromDb = userRepository.findById(id);
        return userFromDb.orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
    }

    @Override
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User update(User user) {
        User userFromDb = userRepository.getById(user.getId());
        userFromDb.setName(user.getName());
        userFromDb.setLastname(user.getLastname());
        userFromDb.setAge(user.getAge());
        userFromDb.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty() && !user.getPassword().equals(userFromDb.getPassword())) {
            userFromDb.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userFromDb.setRoles(user.getRoles());
        return userRepository.save(userFromDb);
    }

    @Override
    @Transactional
    public void save(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null) {
            throw new DuplicateKeyException("Такой пользователь уже существует");
        }
        user.setRoles(user.getRoles());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
