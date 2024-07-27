package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.dto.LoginDTO;
import com.ceylontrail.backend_server.dto.RegisterDTO;
import com.ceylontrail.backend_server.dto.auth.EmailDTO;
import com.ceylontrail.backend_server.dto.auth.OtpDTO;
import com.ceylontrail.backend_server.dto.auth.ResetPasswordDTO;
import com.ceylontrail.backend_server.entity.RoleEntity;
import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.exception.AlreadyExistingException;
import com.ceylontrail.backend_server.exception.BadCredentialException;
import com.ceylontrail.backend_server.exception.NotFoundException;
import com.ceylontrail.backend_server.repo.RoleRepo;
import com.ceylontrail.backend_server.repo.UserRepo;
import com.ceylontrail.backend_server.security.CustomUserDetailsService;
import com.ceylontrail.backend_server.security.webtoken.JwtService;
import com.ceylontrail.backend_server.service.AuthService;
import com.ceylontrail.backend_server.service.MailService;
import com.ceylontrail.backend_server.util.StandardResponse;
import com.ceylontrail.backend_server.util.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthServiceIMPL implements AuthService {

    @Autowired
    private UserRepo userRepo;

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
    private MailService mailService;

    @Override
    public StandardResponse register(RegisterDTO registerDTO) {
        if(userRepo.existsByEmail(registerDTO.getEmail())){
            throw new AlreadyExistingException("Email is already taken!");
        } else if(userRepo.existsByUsername(registerDTO.getUsername())){
            throw new AlreadyExistingException("Username is already taken!");
        } else {
            registerDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
            UserEntity user = mapper.registerDtoToEntity(registerDTO);
            Optional<RoleEntity> roleOptional = roleRepo.findByRoleName("TRAVELLER");
            if (roleOptional.isEmpty()) {
                throw new NotFoundException("Role not found!");
            } else {
                user.setRoles(Collections.singletonList(roleOptional.get()));
                userRepo.save(user);
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
         if (userRepo.existsByUsername(loginDTO.getUsername())){
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
        } else if(userRepo.existsByEmail(loginDTO.getEmail())){
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

    @Override
    public StandardResponse forgetPassword(EmailDTO emailDTO) {
        if (!userRepo.existsByEmail(emailDTO.getEmail()))
            throw new NotFoundException("Email not found!");
        UserEntity user = userRepo.findByEmail(emailDTO.getEmail());
        user.setForgetPasswordOtp(this.otpGenerator());
        user.setForgetPasswordOtpExpiredAt(LocalDateTime.now().plusMinutes(10));
        userRepo.save(user);
        mailService.forgetPasswordOtpMail(user);
        return new StandardResponse(200, "Otp sent successfully", null);
    }

    @Override
    public StandardResponse validateOtp(OtpDTO otpDTO) {
        if (userRepo.existsByForgetPasswordOtp(otpDTO.getOtp()))
            throw new RuntimeException("Otp not found!");
        UserEntity user = userRepo.findByForgetPasswordOtp(otpDTO.getOtp());
        if (user.getForgetPasswordOtpExpiredAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("OTP has expired");
        Map<String, String> userMap = new HashMap<>();
        userMap.put("email", user.getEmail());
        userMap.put("otp", otpDTO.getOtp());
        return new StandardResponse(200, "Otp validated successfully", userMap);
    }

    @Override
    public StandardResponse resetPassword(ResetPasswordDTO resetDTO) {
        if (!userRepo.existsByEmail(resetDTO.getEmail()))
            throw new NotFoundException("Email not found!");
        if (userRepo.existsByForgetPasswordOtp(resetDTO.getOtp()))
            throw new RuntimeException("Otp not found!");
        UserEntity user = userRepo.findByEmail(resetDTO.getEmail());
        user.setForgetPasswordOtp(null);
        user.setForgetPasswordOtpExpiredAt(null);
        user.setPassword(passwordEncoder.encode(resetDTO.getPassword()));
        userRepo.save(user);
        mailService.passwordResetSuccessMail(user);
        return new StandardResponse(200, "Password reset successfully", null);
    }

    public String otpGenerator() {
        SecureRandom random = new SecureRandom();
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        while (true){
            StringBuilder otp = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                char randomChar = CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
                otp.append(randomChar);
            }
            if (userRepo.existsByForgetPasswordOtp(otp.toString())) {
                return otp.toString();
            }
        }
    }

}
