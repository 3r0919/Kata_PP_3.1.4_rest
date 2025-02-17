
package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.model.User;


@Controller
public class UserController {

    @GetMapping("/users")
    public String getSinglePage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("authUser", user);
        return "users";
    }

}