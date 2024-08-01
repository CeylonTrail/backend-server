package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.dto.auth.*;
import com.ceylontrail.backend_server.dto.user.LoggedUserDTO;
import com.ceylontrail.backend_server.entity.RoleEntity;
import com.ceylontrail.backend_server.entity.ServiceProviderEntity;
import com.ceylontrail.backend_server.entity.TravellerEntity;
import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.entity.enums.ServiceProviderTypeEnum;
import com.ceylontrail.backend_server.exception.AlreadyExistingException;
import com.ceylontrail.backend_server.exception.BadCredentialException;
import com.ceylontrail.backend_server.exception.NotFoundException;
import com.ceylontrail.backend_server.repo.RoleRepo;
import com.ceylontrail.backend_server.repo.ServiceProviderRepo;
import com.ceylontrail.backend_server.repo.TravellerRepo;
import com.ceylontrail.backend_server.repo.UserRepo;
import com.ceylontrail.backend_server.security.CustomUserDetail;
import com.ceylontrail.backend_server.security.CustomUserDetailsService;
import com.ceylontrail.backend_server.security.webtoken.JwtService;
import com.ceylontrail.backend_server.service.AuthService;
import com.ceylontrail.backend_server.service.MailService;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

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
    private MailService mailService;
  
    @Autowired
    private TravellerRepo travellerRepo;

    @Autowired
    private ServiceProviderRepo serviceProviderRepo;

    @Override
    public void initialRegisterCheck(String email, String username) {
        if(userRepo.existsByEmail(email))
            throw new AlreadyExistingException("Email is already taken");
        if(userRepo.existsByUsername(username))
            throw new AlreadyExistingException("Username is already taken");
    }

    @Override
    public String activationTokenGenerator() {
        String activationToken;
        while (true){
            activationToken = UUID.randomUUID().toString().replace("-", "");
            if (!userRepo.existsByActivationToken(activationToken))
                return activationToken;
        }
    }

    @Override
    public UserEntity createUser(String email, String username, String password, String firstname, String lastname, String role) {
        this.initialRegisterCheck(email, username);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setActivationToken(this.activationTokenGenerator());
        Optional<RoleEntity> roleOptional = roleRepo.findByRoleName(role);
        if (roleOptional.isEmpty())
            throw new NotFoundException("Role not found");
        else
            user.setRoles(Collections.singletonList(roleOptional.get()));
        return userRepo.save(user);
    }

    @Override
    public String otpGenerator() {
        SecureRandom random = new SecureRandom();
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        while (true){
            StringBuilder otp = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                char randomChar = CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
                otp.append(randomChar);
            }
            if (!userRepo.existsByForgetPasswordOtp(otp.toString()))
                return otp.toString();
        }
    }

    @Override
    @Transactional
    public StandardResponse registerTraveller(TravellerRegisterDTO registerDTO) {
        TravellerEntity traveller = new TravellerEntity();
        traveller.setUser(this.createUser(registerDTO.getEmail(), registerDTO.getUsername(), registerDTO.getPassword(), registerDTO.getFirstname(), registerDTO.getLastname(), "TRAVELLER"));
        travellerRepo.save(traveller);
        mailService.travellerActivationMail(traveller.getUser());
        return new StandardResponse(200, "Registration success", null);
    }

    @Override
    @Transactional
    public StandardResponse registerServiceProvider(ServiceProviderRegisterDTO registerDTO) {
        ServiceProviderEntity serviceProvider = new ServiceProviderEntity();
        serviceProvider.setUser(this.createUser(registerDTO.getEmail(), registerDTO.getUsername(), registerDTO.getPassword(), registerDTO.getFirstname(), registerDTO.getLastname(), "SERVICE_PROVIDER"));
        serviceProvider.setServiceName(registerDTO.getServiceName());
        if (Objects.equals(registerDTO.getServiceType(), String.valueOf(ServiceProviderTypeEnum.ACCOMMODATION)))
            serviceProvider.setServiceType(ServiceProviderTypeEnum.ACCOMMODATION);
        else if (Objects.equals(registerDTO.getServiceType(), String.valueOf(ServiceProviderTypeEnum.RESTAURANT)))
            serviceProvider.setServiceType(ServiceProviderTypeEnum.RESTAURANT);
        else if (Objects.equals(registerDTO.getServiceType(), String.valueOf(ServiceProviderTypeEnum.EQUIPMENT)))
            serviceProvider.setServiceType(ServiceProviderTypeEnum.EQUIPMENT);
        else
            serviceProvider.setServiceType(ServiceProviderTypeEnum.OTHER);
        serviceProviderRepo.save(serviceProvider);
        mailService.serviceProviderActivationMail(serviceProvider);
        return new StandardResponse(200, "Registration success", null);
    }

    @Override
    public StandardResponse login(LoginDTO loginDTO) {
         if(userRepo.existsByEmail(loginDTO.getEmail())) {
             Authentication authentication;
             try {
                 authentication = authenticationManager.authenticate(
                         new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
                 );
             } catch (BadCredentialsException e) {
                 throw new BadCredentialException("Password is incorrect");
             }
             if (authentication.isAuthenticated()) {
                 UserEntity user = userRepo.findByEmail(loginDTO.getEmail());
                 LoggedUserDTO loggedUserDTO = new LoggedUserDTO(
                         jwtService.generateToken(customUserDetailsService.loadUserByUsername(loginDTO.getEmail())),
                         user.getUserId(),
                         user.getUsername(),
                         user.getEmail(),
                         user.getFirstname(),
                         user.getLastname(),
                         user.getActivationToken() == null,
                         user.getRoles().get(0).getRoleName(),
                         user.getProfilePictureUrl()
                 );
                 return new StandardResponse(200, "Login success", loggedUserDTO);
             }
         } else throw new NotFoundException("Email not found");
        return new StandardResponse(500, "Internal server error", null);
    }

    @Override
    public StandardResponse activate(ActivationTokenDTO tokenDTO) {
       if (!userRepo.existsByActivationToken(tokenDTO.getActivationToken()))
           throw new NotFoundException("Activation token not found");
       UserEntity user = userRepo.findByActivationToken(tokenDTO.getActivationToken());
       user.setActivationToken(null);
       userRepo.save(user);
       return new StandardResponse(200, "Account activate successfully", null);
    }

    @Override
    public StandardResponse forgetPassword(EmailDTO emailDTO) {
        if (!userRepo.existsByEmail(emailDTO.getEmail()))
            throw new NotFoundException("Email not found");
        UserEntity user = userRepo.findByEmail(emailDTO.getEmail());
        user.setForgetPasswordOtp(this.otpGenerator());
        user.setForgetPasswordOtpExpiredAt(LocalDateTime.now().plusMinutes(10));
        userRepo.save(user);
        mailService.forgetPasswordOtpMail(user);
        return new StandardResponse(200, "Otp sent successfully", null);
    }

    @Override
    public StandardResponse validateOtp(OtpDTO otpDTO) {
        if (!userRepo.existsByForgetPasswordOtp(otpDTO.getOtp()))
            throw new NotFoundException("Otp not found");
        UserEntity user = userRepo.findByForgetPasswordOtp(otpDTO.getOtp());
        if (user.getForgetPasswordOtpExpiredAt().isBefore(LocalDateTime.now()))
            throw new BadCredentialException("OTP has expired");
        Map<String, String> userMap = new HashMap<>();
        userMap.put("email", user.getEmail());
        userMap.put("otp", otpDTO.getOtp());
        return new StandardResponse(200, "Otp validated successfully", userMap);
    }

    @Override
    public StandardResponse resetPassword(ResetPasswordDTO resetDTO) {
        if (!userRepo.existsByEmail(resetDTO.getEmail()))
            throw new NotFoundException("Email not found");
        if (!userRepo.existsByForgetPasswordOtp(resetDTO.getOtp()))
            throw new NotFoundException("Otp not found");
        UserEntity user = userRepo.findByEmail(resetDTO.getEmail());
        user.setForgetPasswordOtp(null);
        user.setForgetPasswordOtpExpiredAt(null);
        user.setPassword(passwordEncoder.encode(resetDTO.getPassword()));
        userRepo.save(user);
        mailService.passwordResetSuccessMail(user);
        return new StandardResponse(200, "Password reset successfully", null);
    }

    @Override
    public Integer getAuthUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetail) {
            return ((CustomUserDetail) principal).getId();
        }
        return 0;
    }

}
