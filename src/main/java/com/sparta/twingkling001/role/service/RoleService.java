package com.sparta.twingkling001.role.service;

import com.sparta.twingkling001.api.response.ApiResponse;
import com.sparta.twingkling001.role.dto.response.RoleNameResponseDto;
import com.sparta.twingkling001.role.entity.Role;
import com.sparta.twingkling001.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getRole(long roleId) {
        return roleRepository.findById(roleId).orElse(null);
    }
}
