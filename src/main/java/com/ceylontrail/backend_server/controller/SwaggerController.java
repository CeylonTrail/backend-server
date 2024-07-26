package com.ceylontrail.backend_server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerController {
    @GetMapping("/login")
    public String handleSwagger(){
        return "custom_login";
    }
}
