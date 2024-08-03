package com.example.Platform.service;

import com.example.Platform.entity.Role;

import java.util.List;

public interface RoleService {
    Role getRoleById(Long id);

    List<Role> getAllRole();
}
