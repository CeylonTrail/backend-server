package com.ceylontrail.backend_server.security;

import com.ceylontrail.backend_server.entity.RoleEntity;
import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public CustomUserDetail loadUserByUsername(String email) {
        if (!userRepo.existsByEmail(email))
            throw new UsernameNotFoundException("Email not found");
        UserEntity userEntity = userRepo.findByEmail(email);
        return new CustomUserDetail(userEntity.getEmail(), userEntity.getPassword(),mapRolesToAuthorities(userEntity.getRoles()),userEntity.getUserId());
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<RoleEntity> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toList());
    }
}

