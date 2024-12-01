package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.dto.auth.*;
import com.ceylontrail.backend_server.dto.auth.LoggedAdminDTO;
import com.ceylontrail.backend_server.dto.auth.LoggedSPDTO;
import com.ceylontrail.backend_server.dto.auth.LoggedTravellerDTO;
import com.ceylontrail.backend_server.entity.RoleEntity;
import com.ceylontrail.backend_server.entity.ServiceProviderEntity;
import com.ceylontrail.backend_server.entity.TravellerEntity;
import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.entity.enums.ServiceProviderTypeEnum;
import com.ceylontrail.backend_server.entity.enums.VerificationStatusEnum;
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
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class AuthServiceIMPL implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    private final JwtService jwtService;
    private final MailService mailService;

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final ServiceProviderRepo spRepo;
    private final TravellerRepo travellerRepo;

    private void initialRegisterCheck(String email, String username) {
        if(userRepo.existsByEmail(email))
            throw new AlreadyExistingException("Email is already taken");
        if(userRepo.existsByUsername(username))
            throw new AlreadyExistingException("Username is already taken");
    }

    private String activationTokenGenerator() {
        String activationToken;
        while (true){
            activationToken = UUID.randomUUID().toString().replace("-", "");
            if (!userRepo.existsByActivationToken(activationToken))
                return activationToken;
        }
    }

    private UserEntity createUser(String email, String username, String password, String firstname, String lastname, String role) {
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
        if (Objects.equals(role, "TRAVELLER"))
            user.setIsTraveller("YES");
        else
            user.setIsTraveller("NO");
        return userRepo.save(user);
    }

    private String otpGenerator() {
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
        serviceProvider.setIsSetupComplete("NO");
        spRepo.save(serviceProvider);
        mailService.serviceProviderActivationMail(serviceProvider);
        return new StandardResponse(200, "Registration success", null);
    }

    @Override
    public StandardResponse login(LoginDTO loginDTO) {
         if(userRepo.existsByEmail(loginDTO.getEmail())) {
             Authentication authentication;
             try {
                 authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
             } catch (BadCredentialsException e) {
                 throw new BadCredentialException("Password is incorrect");
             }
             if (authentication.isAuthenticated()) {
                 UserEntity user = userRepo.findByEmail(loginDTO.getEmail());
                 if (Objects.equals(user.getRoles().get(0).getRoleName(), "ADMIN")){
                     LoggedAdminDTO adminDTO = new LoggedAdminDTO();
                     adminDTO.setAccessToken(this.jwtService.generateToken(customUserDetailsService.loadUserByUsername(user.getEmail())));
                     adminDTO.setUserId(user.getUserId());
                     adminDTO.setUsername(user.getUsername());
                     adminDTO.setEmail(user.getEmail());
                     return new StandardResponse(200, "Admin login success", adminDTO);
                 } else if (Objects.equals(user.getRoles().get(0).getRoleName(), "SERVICE_PROVIDER")) {
                     ServiceProviderEntity sp = user.getServiceProvider();
                     LoggedSPDTO spDTO = new LoggedSPDTO();
                     spDTO.setAccessToken(this.jwtService.generateToken(customUserDetailsService.loadUserByUsername(user.getEmail())));
                     spDTO.setUserId(user.getUserId());
                     spDTO.setServiceProviderId(sp.getServiceProviderId());
                     spDTO.setUsername(user.getUsername());
                     spDTO.setEmail(user.getEmail());
                     spDTO.setServiceName(sp.getServiceName());
                     if (Objects.equals(sp.getServiceType(), ServiceProviderTypeEnum.ACCOMMODATION))
                         spDTO.setServiceType(String.valueOf(ServiceProviderTypeEnum.ACCOMMODATION));
                     else if (Objects.equals(sp.getServiceType(), ServiceProviderTypeEnum.RESTAURANT))
                         spDTO.setServiceType(String.valueOf(ServiceProviderTypeEnum.RESTAURANT));
                     else if (Objects.equals(sp.getServiceType(), ServiceProviderTypeEnum.EQUIPMENT))
                         spDTO.setServiceType(String.valueOf(ServiceProviderTypeEnum.EQUIPMENT));
                     else
                         spDTO.setServiceType(String.valueOf(ServiceProviderTypeEnum.OTHER));
                     spDTO.setFirstname(user.getFirstname());
                     spDTO.setLastname(user.getLastname());
                     spDTO.setAccountState(user.getActivationToken() == null);
                     if (Objects.equals(sp.getIsSetupComplete(), "NO")) {
                         spDTO.setSetupState(false);
                         return new StandardResponse(200, "Service Provider login success with no setup", spDTO);
                     } else {
                         spDTO.setSetupState(true);
                         if (Objects.equals(sp.getVerificationStatus(), VerificationStatusEnum.PENDING))
                             spDTO.setVerificationStatus(String.valueOf(VerificationStatusEnum.PENDING));
                         else if (Objects.equals(sp.getVerificationStatus(), VerificationStatusEnum.REJECTED))
                             spDTO.setVerificationStatus(String.valueOf(VerificationStatusEnum.REJECTED));
                         else
                             spDTO.setVerificationStatus(String.valueOf(VerificationStatusEnum.APPROVED));
                         spDTO.setDescription(sp.getDescription());
                         spDTO.setContactNumber(sp.getContactNumber());
                         spDTO.setAddress(sp.getAddress());
                         spDTO.setProfilePictureUrl(user.getProfilePictureUrl());
                         spDTO.setCoverPictureUrl(sp.getCoverPictureUrl());
                         spDTO.setSocialMediaLinks(sp.getSocialMediaLinks());
                         spDTO.setOpeningHours(sp.getOpeningHours());
                         return new StandardResponse(200, "Service Provider login success with setup", spDTO);
                     }
                 } else {
                     LoggedTravellerDTO travellerDTO = new LoggedTravellerDTO();
                     travellerDTO.setAccessToken(this.jwtService.generateToken(customUserDetailsService.loadUserByUsername(user.getEmail())));
                     travellerDTO.setUserId(user.getUserId());
                     travellerDTO.setUsername(user.getUsername());
                     travellerDTO.setEmail(user.getEmail());
                     travellerDTO.setFirstname(user.getFirstname());
                     travellerDTO.setLastname(user.getLastname());
                     travellerDTO.setAccountState(user.getActivationToken() == null);
                     travellerDTO.setProfilePictureUrl(user.getProfilePictureUrl());
                     return new StandardResponse(200, "Traveller login success", travellerDTO);
                 }
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
