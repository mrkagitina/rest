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
import ru.kata.spring.boot_security.demo.DTO.UserDto;
import ru.kata.spring.boot_security.demo.DTO.UserMapper;
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

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
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
    public UserDto update(UserDto userDto) {
        User userFromDb = userRepository.getById(userDto.getId());
        userFromDb.setName(userDto.getName());
        userFromDb.setLastname(userDto.getLastname());
        userFromDb.setAge(userDto.getAge());
        userFromDb.setEmail(userDto.getEmail());

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty() &&
                !userDto.getPassword().equals(userFromDb.getPassword())) {
            userFromDb.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        userFromDb.setRoles(userDto.getRoles());
        User updatedUser = userRepository.save(userFromDb);
        return convertToDto(updatedUser);
    }

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        User userFromDb = userRepository.findByUsername(userDto.getName());
        if (userFromDb != null) {
            throw new DuplicateKeyException("Такой пользователь уже существует");
        }

        User newUser = new User();
        newUser.setName(userDto.getName());
        newUser.setLastname(userDto.getLastname());
        newUser.setAge(userDto.getAge());
        newUser.setName(userDto.getName());
        newUser.setEmail(userDto.getEmail());
        newUser.setRoles(userDto.getRoles());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User savedUser = userRepository.save(newUser);
        return convertToDto(savedUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public UserDto convertToDto(User userFromDb) {
        return userMapper.convertToDto(userFromDb);
    }
}
