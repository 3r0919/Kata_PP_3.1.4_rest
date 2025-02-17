package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    void add(Role role);

    List<Role> findAll();

    Optional<Role> getRoleById(Long id);

    Role getRoleByName(String name);

}

