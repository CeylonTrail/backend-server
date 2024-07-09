package com.ceylontrail.backend_server.service.impl;
import com.ceylontrail.backend_server.dto.LoginDTO;
import com.ceylontrail.backend_server.dto.RegisterDTO;
import com.ceylontrail.backend_server.entity.RoleEntity;
import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.repo.RoleRepo;
import com.ceylontrail.backend_server.repo.UserRepo;
import com.ceylontrail.backend_server.security.CustomUserDetailsService;
import com.ceylontrail.backend_server.security.webtoken.JwtService;
import com.ceylontrail.backend_server.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Optional;

@Service
public class AuthServiceIMPL implements AuthService {
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public String register(RegisterDTO registerDto) {
        if(userRepository.existsByUsername(registerDto.getUsername())){
            return "Username is taken!";
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Optional<RoleEntity> roleOptional = roleRepo.findByName("TRAVELLER");
        if (roleOptional.isEmpty()) {
            return "Role not found!";
        }
        user.setRoles(Collections.singletonList(roleOptional.get()));
        userRepository.save(user);
        return "User registered successfully!";
    }

    @Override
    public String login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(),
                loginDTO.getPassword()
        ));
        System.out.println(authentication);
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(customUserDetailsService.loadUserByUsername(loginDTO.getUsername()));

        }else {
            throw  new UsernameNotFoundException("Username or password is incorrect!");
        }
    }
}
