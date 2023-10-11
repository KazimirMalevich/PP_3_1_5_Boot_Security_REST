package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.repositories.UserRepo;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.Optional;


@Service
@Transactional
public class UserDetailsServiceImp implements UserDetailsService {
    private final UserRepo userRepo;
    private final UserService userService;


    public UserDetailsServiceImp(UserRepo userRepo, UserService userService) {
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> user = Optional.ofNullable(userRepo.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found")));
        return user.get();
    }
}

