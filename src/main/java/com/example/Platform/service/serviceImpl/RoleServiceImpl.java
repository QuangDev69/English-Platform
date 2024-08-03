package com.example.Platform.service.serviceImpl;

import com.example.Platform.entity.Role;
import com.example.Platform.repository.RoleRepository;
import com.example.Platform.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public Role getRoleById(Long id) {
        return null;
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }
}
