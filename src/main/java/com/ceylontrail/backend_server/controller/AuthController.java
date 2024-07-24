package com.ceylontrail.backend_server.controller;

import com.ceylontrail.backend_server.dto.LoginDTO;
import com.ceylontrail.backend_server.dto.RegisterDTO;
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

    @PostMapping("/register")
    public StandardResponse register(@Valid @RequestBody RegisterDTO registerDTO) {
        return authService.register(registerDTO);
    }

    @PostMapping("/login")
    public StandardResponse login(@Valid @RequestBody LoginDTO loginDTO){
        return authService.login(loginDTO);
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
        int userID = getAuthenticatedId();
        return ResponseEntity.ok("Welcome to Ceylon Trail Traveller, "+ userID);
    }


    @GetMapping("/admin/home")
    public String handleWelcomeAdmin(){
        int userID = getAuthenticatedId();
        return "Welcome to Ceylon Trail Admin" + userID;
    }


    public int getAuthenticatedId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetail) {
            return ((CustomUserDetail) principal).getId();
        }
        return 0;
    }

}
