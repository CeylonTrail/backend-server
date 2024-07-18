package com.ceylontrail.backend_server.controller;

import com.ceylontrail.backend_server.dto.AuthResponseDTO;
import com.ceylontrail.backend_server.dto.LoginDTO;
import com.ceylontrail.backend_server.dto.RegisterDTO;
import com.ceylontrail.backend_server.repo.UserRepo;
import com.ceylontrail.backend_server.security.CustomUserDetail;
import com.ceylontrail.backend_server.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepo userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDTO registerDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(String.join("\n", errorMessages));
        } else {
            AuthResponseDTO responseDTO = authService.register(registerDTO);
            if (responseDTO.getStatus().equals("ok")){
                return ResponseEntity.ok().body(responseDTO.toString());
            } else {
                return ResponseEntity.badRequest().body(responseDTO.toString());
            }
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(String.join("\n", errorMessages));
        } else {
            AuthResponseDTO responseDTO = authService.login(loginDTO);
            if (responseDTO.getStatus().equals("ok")){
                return ResponseEntity.ok().body(responseDTO.toString());
            } else {
                return ResponseEntity.badRequest().body(responseDTO.toString());
            }
        }
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
