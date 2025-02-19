package ru.kata.spring.boot_security.demo.mapper;

import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;

public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
    void updateUserFromDTO(UserDTO userDTO, User user);

}
