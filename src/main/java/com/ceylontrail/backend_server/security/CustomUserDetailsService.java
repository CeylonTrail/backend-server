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

import javax.management.relation.Role;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userEntityRepo;
    @Override
    public CustomUserDetail loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity = userEntityRepo.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new CustomUserDetail(userEntity.getUsername(), userEntity.getPassword(),mapRolesToAuthorities(userEntity.getRoles()),userEntity.getUserId());
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<RoleEntity> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toList());
    }
}

