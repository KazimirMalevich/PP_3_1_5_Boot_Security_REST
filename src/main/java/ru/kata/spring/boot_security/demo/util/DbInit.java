package ru.kata.spring.boot_security.demo.util;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
public class DbInit {

    private final RoleService roleService;
    private final UserService userService;

    public DbInit(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }


    @PostConstruct
    public void addRolesAndUserInit() {
        if (roleService.allRoles().isEmpty() & userService.getAllUsers().isEmpty()) {
            Role userRole = new Role("ROLE_USER");
            Role adminRole = new Role("ROLE_ADMIN");
            roleService.addRole(userRole);
            roleService.addRole(adminRole);
            User user = new User("admin", "admin", 32, "admin@mail.ru", "admin");
            user.setRoles(Set.of(adminRole));
            userService.saveUser(user);
//            userService.saveUser(new User("admin", "admin", 32, "admin@mail.ru", "admin"));



        }
    }
}