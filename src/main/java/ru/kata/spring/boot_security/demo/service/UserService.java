package ru.kata.spring.boot_security.demo.service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void add(User user);

    void update(UserDTO userDTO);

    void removeById(Long id);

    List<UserDTO> findAll();

    public User findByIdWithRoles(Long id);

    Optional<User> findById(Long id);

    void updateUserFields(User user, UserDTO userDTO);

}
