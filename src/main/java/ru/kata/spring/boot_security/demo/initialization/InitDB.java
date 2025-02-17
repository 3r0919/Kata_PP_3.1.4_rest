package ru.kata.spring.boot_security.demo.initialization;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class InitDB {

    private final RoleService roleService;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(InitDB.class);

    public InitDB(RoleService roleService, UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    protected void fillDb() {
        if (roleService.findAll().isEmpty()) {
            Role roleAdmin = new Role("ROLE_ADMIN");
            Role roleUser = new Role("ROLE_USER");

            roleService.add(roleAdmin);
            roleService.add(roleUser);
            logger.info("Roles added: ROLE_ADMIN, ROLE_USER");
        } else {
            logger.info("Roles already exist in the database.");
        }

        if (userService.findAll().isEmpty()) {
            User user = new User("user@example.com", "User", "User", 25, passwordEncoder.encode("user123"));
            user.addRole(roleService.getRoleByName("ROLE_USER"));
            userService.add(user);
            logger.info("User added: user@example.com");

            User admin = new User("admin@example.com", "Admin", "Admin", 32, passwordEncoder.encode("admin123"));
            admin.addRole(roleService.getRoleByName("ROLE_ADMIN"));
            userService.add(admin);
            logger.info("Admin added: admin@example.com");
        } else {
            logger.info("Users already exist in the database.");
        }
    }
}
