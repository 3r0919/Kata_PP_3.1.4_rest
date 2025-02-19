package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {

    private final RoleMapper roleMapper;

    public UserMapperImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAge(),
                user.getRoles() != null ? user.getRoles().stream()
                        .map(roleMapper::toDTO)
                        .collect(Collectors.toSet()) : new HashSet<>()
        );
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAge(userDTO.getAge());
        user.setRoles(userDTO.getRoles() != null ? userDTO.getRoles().stream()
                .map(roleMapper::toEntity)
                .collect(Collectors.toSet()) : new HashSet<>());
        return user;
    }

    @Override
    public void updateUserFromDTO(UserDTO userDTO, User user) {
        if (userDTO == null || user == null) {
            return;
        }
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAge(userDTO.getAge());
    }

}