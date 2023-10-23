package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.exception_handling.NoSuchUserException;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.Set;

@Controller
@RequestMapping("/")
public class MyRESTController {
    private final UserService userService;
    private final RoleService roleService;

    public MyRESTController(UserService userService, RoleService roleService) {
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

    @GetMapping(value = "login")
    public String getLoginPage() {
        return "loginPage";
    }

    @GetMapping(value = "user")
    public String enterUser(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByUsername(auth.getName());
        model.addAttribute("authUser", authUser);
        System.out.println(authUser);
        return "userPage2";
    }

    @ResponseBody
    @GetMapping("api/auth")
    public User getAythUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userService.getUserByUsername(auth.getName());
        return authUser;
    }

    @ResponseBody
    @GetMapping("api/users")
    public Set<User> showAllUsers() {
        return userService.getAllUsers();
    }

    @ResponseBody
    @GetMapping("api/users/{id}")
    public User getUserFromDB(@PathVariable Integer id) {
        User user = userService.getUser(id);
        if (user==null) {
            throw new NoSuchUserException("No User with ID = " + id + " in DataBase");
        }
        return user;
    }

    @ResponseBody
    @GetMapping("api/users/roles")
    public ResponseEntity<Set<Role>> getAllRoles(){
        Set <Role> roleList = roleService.allRoles();
        return new ResponseEntity<>(roleList,HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("api/users")
    public ResponseEntity<HttpStatus> addNewUser(@RequestBody User user) {
       userService.saveUser(user);
       return ResponseEntity.ok(HttpStatus.OK);
    }

    @ResponseBody
    @PatchMapping("api/users")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody User user) {
        userService.update(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping("api/users/{id}")
    public String deleteUser(@PathVariable Integer id) {
        User user = userService.getUser(id);
        if (user == null) {
            throw new NoSuchUserException("No user with ID = " + id +" in DataBase");
        }
        userService.deleteUser(id);
        return "User with ID = " + id + " delete";
    }
}
