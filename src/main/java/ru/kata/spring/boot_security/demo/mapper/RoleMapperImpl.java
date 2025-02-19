package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.model.Role;

@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public RoleDTO toDTO(Role role) {
        if (role == null) {
            return null;
        }
        return new RoleDTO(
                role.getId(),
                role.getName()
        );
    }

    @Override
    public Role toEntity(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        }
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setName(roleDTO.getName());
        return role;
    }
}