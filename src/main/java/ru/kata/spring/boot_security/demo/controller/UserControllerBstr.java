package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/user")
public class UserControllerBstr {
    private final UserService userService;

    public UserControllerBstr(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "")
    public String enterUser(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByUsername(auth.getName());
        model.addAttribute("authUser", authUser);
        System.out.println(authUser);
        return "userPage2";
    }
}

