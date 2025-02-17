package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u join fetch u.roles where u.email = :email")
    Optional<User> findUserAndFetchRoles(@Param("email") String email);
    @Query("SELECT u FROM User u JOIN FETCH u.roles")
    List<User> findAllWithRoles();
}