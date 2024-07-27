package com.ceylontrail.backend_server.service.impl;
import com.ceylontrail.backend_server.dto.LoginDTO;
import com.ceylontrail.backend_server.dto.RegisterDTO;
import com.ceylontrail.backend_server.entity.RoleEntity;
import com.ceylontrail.backend_server.entity.TravellerEntity;
import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.exception.AlreadyExistingException;
import com.ceylontrail.backend_server.exception.BadCredentialException;
import com.ceylontrail.backend_server.exception.NotFoundException;
import com.ceylontrail.backend_server.repo.RoleRepo;
import com.ceylontrail.backend_server.repo.TravellerRepo;
import com.ceylontrail.backend_server.repo.UserRepo;
import com.ceylontrail.backend_server.security.CustomUserDetailsService;
import com.ceylontrail.backend_server.security.webtoken.JwtService;
import com.ceylontrail.backend_server.service.AuthService;
import com.ceylontrail.backend_server.util.StandardResponse;
import com.ceylontrail.backend_server.util.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    @Autowired
    private Mapper mapper;

    @Autowired
    private TravellerRepo travellerRepo;

    @Override
    public StandardResponse register(RegisterDTO registerDTO) {
        if(userRepository.existsByEmail(registerDTO.getEmail())){
            throw new AlreadyExistingException("Email is already taken!");
        } else if(userRepository.existsByUsername(registerDTO.getUsername())){
            throw new AlreadyExistingException("Username is already taken!");
        } else {
            registerDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
            UserEntity user = mapper.registerDtoToEntity(registerDTO);

//            user.setEmail(registerDTO.getEmail());
//            user.setUsername(registerDTO.getUsername());
//            user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
//            user.setFirstname(registerDTO.getFirstname());
//            user.setLastname(registerDTO.getLastname());
            Optional<RoleEntity> roleOptional = roleRepo.findByRoleName("TRAVELLER");
            if (roleOptional.isEmpty()) {
                throw new NotFoundException("Role not found!");
            } else {
                user.setRoles(Collections.singletonList(roleOptional.get()));
                userRepository.save(user);

                TravellerEntity traveller = new TravellerEntity(
                        userRepository.getReferenceById(user.getUserId()),
                        user.getFirstname(),
                        user.getLastname()
                );
                travellerRepo.save(traveller);

                Map<String, String> userMap = new HashMap<>();
                userMap.put("email", user.getEmail());
                userMap.put("username", user.getUsername());
                userMap.put("firstname", user.getFirstname());
                userMap.put("lastname", user.getLastname());
                return new StandardResponse(200, "Registration success", userMap);

            }


        }
    }

    // <<<BUG>>> Email login feature currently not working!!!
    @Override
    public StandardResponse login(LoginDTO loginDTO) {
         if (userRepository.existsByUsername(loginDTO.getUsername())){
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDTO.getUsername(),
                                loginDTO.getPassword())
                );
                if (authentication.isAuthenticated()) {
                    Map<String, String> tokenMap = new HashMap<>();
                    tokenMap.put("token", jwtService.generateToken(customUserDetailsService.loadUserByUsername(loginDTO.getUsername())));
                    return new StandardResponse(200, "Login success", tokenMap);
                }
            } catch (BadCredentialsException e) {
                throw new BadCredentialException("Password is incorrect!");
            }
        } else if(userRepository.existsByEmail(loginDTO.getEmail())){
             try {
                 Authentication authentication = authenticationManager.authenticate(
                         new UsernamePasswordAuthenticationToken(
                                 loginDTO.getEmail(),
                                 loginDTO.getPassword())
                 );
                 if (authentication.isAuthenticated()) {
                     Map<String, String> tokenMap = new HashMap<>();
                     tokenMap.put("token", jwtService.generateToken(customUserDetailsService.loadUserByUsername(loginDTO.getUsername())));
                     return new StandardResponse(200, "Login success", tokenMap);
                 }
             } catch (BadCredentialsException e) {
                 throw new BadCredentialException("Password is incorrect!");
             }
         } else throw new NotFoundException("Both email and username not found!");
        return null;
    }
}
