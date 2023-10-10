package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kata.spring.boot_security.demo.repositories.RoleRepo;
import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private RoleRepo roleRepo;

    @Autowired
    public void setRoleRepo(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;

    }

    @Override
    public Role getRoleById(Integer id) {
        return roleRepo.getById(id);
    }

    @Override
    public Set<Role> allRoles() {
        return new HashSet<>(roleRepo.findAll());
    }


    @Override
    public void addRole(Role role) {
        roleRepo.save(role);
    }
}
