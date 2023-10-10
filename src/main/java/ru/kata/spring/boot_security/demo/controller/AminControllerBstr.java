package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/")
public class AminControllerBstr {
    private final UserService userService;
    private final RoleService roleService;

    public AminControllerBstr(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "admin")
    public String allUsers(ModelMap model) {
        model.addAttribute("users", userService.getAllUsers());
        User user = new User();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByUsername(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("roless", roleService.allRoles());
        model.addAttribute("authUser", authUser);
        return "adminPage";
    }

    @PostMapping(value = "admin/add")
    public String postAddUser(@ModelAttribute("newUser") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("admin/delete/{id}")
    public String deleteUserById(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PatchMapping("admin/edit")
    public String postEditUser(@ModelAttribute("user") User user) {

        userService.update(user);

        return "redirect:/admin";
    }
}

