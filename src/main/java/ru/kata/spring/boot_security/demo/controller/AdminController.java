package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.exception_handling.NoSuchRoleException;
import ru.kata.spring.boot_security.demo.exception_handling.NoSuchUserException;
import ru.kata.spring.boot_security.demo.mapper.RoleMapper;
import ru.kata.spring.boot_security.demo.mapper.UserMapper;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final RoleMapper roleMapper;
    private final UserMapper userMapper;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, RoleMapper roleMapper, UserMapper userMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}/roles")
    public ResponseEntity<Set<Role>> getUserRoles(@PathVariable Long id) {
        User user = userService.findByIdWithRoles(id);
        return new ResponseEntity<>(user.getRoles(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new NoSuchUserException("User with id " + id + " not found"));
        UserDTO userDTO = userMapper.toDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@Validated @RequestBody UserDTO userDTO) {
        User user = new User();
        userService.updateUserFields(user, userDTO);
        Set<Role> roles = userDTO.getRoles() != null ? userDTO.getRoles().stream()
                .map(roleDTO -> new Role(roleDTO.getId(), roleDTO.getName()))
                .collect(Collectors.toSet()) : new HashSet<>();
        user.setRoles(roles);
        userService.add(user);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> saveUser(@PathVariable Long id, @RequestBody UserDTO updateUserDTO) {

        User oldUser = userService.findById(id)
                .orElseThrow(() -> new NoSuchUserException("User with id " + id + " not found"));
        if (updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().isEmpty()) {
            userService.updateUserFields(oldUser, updateUserDTO);
        }
        if (updateUserDTO.getRoles() != null && !updateUserDTO.getRoles().isEmpty()) {
            Set<Role> roleSet = updateUserDTO.getRoles().stream()
                    .map(roleMapper::toEntity)
                    .collect(Collectors.toSet());
            oldUser.setRoles(roleSet);
        }

        userService.update(updateUserDTO);
        return new ResponseEntity<>(updateUserDTO, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.removeById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id)
                .map(role -> new ResponseEntity<>(role, HttpStatus.OK))
                .orElseThrow(() -> new NoSuchRoleException("Role with id " + id + " not found"));
    }

}