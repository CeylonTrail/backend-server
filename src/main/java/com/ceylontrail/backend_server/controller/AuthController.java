package com.ceylontrail.backend_server.controller;

import com.ceylontrail.backend_server.dto.LoginDTO;
import com.ceylontrail.backend_server.dto.RegisterDTO;
import com.ceylontrail.backend_server.repo.UserRepo;
import com.ceylontrail.backend_server.security.CustomUserDetail;
import com.ceylontrail.backend_server.service.AuthService;
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
    @Autowired
    private UserRepo userRepository;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDto) {
        String id = authService.register(registerDto);
        return ResponseEntity.ok(id);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO){
        String token = authService.login(loginDTO);
        return ResponseEntity.ok(token);
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
        int userID = getAuthenticatedUsername();
        return ResponseEntity.ok("Welcome to Ceylon Trail Traveller, "+ userID);
    }


    @GetMapping("/admin/home")
    public String handleWelcomeAdmin(){
        int userID = getAuthenticatedUsername();
        return "Welcome to Ceylon Trail Admin" + userID;
    }


    private Integer getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetail) {
            return ((CustomUserDetail) principal).getId();
        }
        return 0;
    }

}
