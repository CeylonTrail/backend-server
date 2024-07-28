package com.ceylontrail.backend_server.controller;

import com.ceylontrail.backend_server.dto.auth.*;
import com.ceylontrail.backend_server.security.CustomUserDetail;
import com.ceylontrail.backend_server.service.AuthService;
import com.ceylontrail.backend_server.util.StandardResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register-traveller")
    public StandardResponse registerTraveller(@Valid @RequestBody TravellerRegisterDTO registerDTO) {
        return authService.registerTraveller(registerDTO);
    }

    @PostMapping("/register-service-provider")
    public StandardResponse registerServiceProvider(@Valid @RequestBody ServiceProviderRegisterDTO registerDTO) {
        return authService.registerServiceProvider(registerDTO);
    }

    @PostMapping("/login")
    public StandardResponse login(@Valid @RequestBody LoginDTO loginDTO){
        return authService.login(loginDTO);
    }

    @PostMapping("/forget-password")
    public StandardResponse forgetPassword(@Valid @RequestBody EmailDTO emailDTO) {
        return authService.forgetPassword(emailDTO);
    }

    @PostMapping("/validate-otp")
    public StandardResponse validateOtp(@Valid @RequestBody OtpDTO otpDTO) {
        return authService.validateOtp(otpDTO);
    }

    @PostMapping("/reset-password")
    public StandardResponse resetPassword(@Valid ResetPasswordDTO resetDTO) {
        return authService.resetPassword(resetDTO);
    }

    @GetMapping("/home")
    public String handleWelcome(){
        return "hello sign in to view";
    }

    @GetMapping("/traveller/home")
    public String handleWelcomeTraveller(){
        return "Welcome to Ceylon Trail Traveller";
    }
    @GetMapping("/traveller/details")
    public ResponseEntity<String> handleTraveller() {
        int userID = getAuthenticatedUserId();
        return ResponseEntity.ok("Welcome to Ceylon Trail Traveller, "+ userID);
    }


    @GetMapping("/admin/home")
    public String handleWelcomeAdmin(){
        int userID = getAuthenticatedUserId();
        return "Welcome to Ceylon Trail Admin" + userID;
    }

    public Integer getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetail) {
            return ((CustomUserDetail) principal).getId();
        }
        return 0;
    }

}
