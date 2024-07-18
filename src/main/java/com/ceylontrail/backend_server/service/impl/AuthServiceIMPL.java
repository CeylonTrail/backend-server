package com.ceylontrail.backend_server.service.impl;
import com.ceylontrail.backend_server.dto.AuthResponseDTO;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
    public AuthResponseDTO register(RegisterDTO registerDTO) {
        if(userRepository.existsByEmail(registerDTO.getEmail())){
            return new AuthResponseDTO("bad", "Email already exists!");
        } else if(userRepository.existsByUserName(registerDTO.getUserName())){
            return new AuthResponseDTO("bad", "UserName is already taken!");
        } else {
            UserEntity user = new UserEntity();
            user.setEmail(registerDTO.getEmail());
            user.setUserName(registerDTO.getUserName());
            user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
            user.setFirstName(registerDTO.getFirstName());
            user.setLastName(registerDTO.getLastName());
            Optional<RoleEntity> roleOptional = roleRepo.findByRoleName("TRAVELLER");
            if (roleOptional.isEmpty()) {
                return new AuthResponseDTO("bad", "Role Not Found!");
            } else {
                user.setRoles(Collections.singletonList(roleOptional.get()));
                userRepository.save(user);
                return new AuthResponseDTO("bad", "User Registered!");
            }
        }
    }

    // <<<BUG>>> Email login feature currently not working!!!
    @Override
    public AuthResponseDTO login(LoginDTO loginDTO) {
         if (userRepository.existsByUserName(loginDTO.getUserName())){
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDTO.getUserName(),
                                loginDTO.getPassword())
                );
                if (authentication.isAuthenticated()) {
                    return new AuthResponseDTO("ok", jwtService.generateToken(customUserDetailsService.loadUserByUsername(loginDTO.getUserName())));
                }
            } catch (BadCredentialsException e) {
                return new AuthResponseDTO("bad", "Password is incorrect!");
            }
        } else if(userRepository.existsByEmail(loginDTO.getEmail())){
             try {
                 Authentication authentication = authenticationManager.authenticate(
                         new UsernamePasswordAuthenticationToken(
                                 loginDTO.getEmail(),
                                 loginDTO.getPassword())
                 );
                 if (authentication.isAuthenticated()) {
                     return new AuthResponseDTO("ok", jwtService.generateToken(customUserDetailsService.loadUserByUsername(loginDTO.getUserName())));
                 }
             } catch (BadCredentialsException e) {
                 return new AuthResponseDTO("bad", "Password is incorrect!");
             }
         } else return new AuthResponseDTO("bad", "Both Email and Username is Not Found!");
        return null;
    }
}
