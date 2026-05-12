package com.Croquetas.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/debug-role")
    @ResponseBody
    public String debugRole(Authentication auth) {
        if (auth == null) return "Not authenticated";
        return "User: " + auth.getName() + ", Authorities: " + auth.getAuthorities();
    }
    @GetMapping("/whoami")
    @ResponseBody
    public String whoami(Authentication auth) {
        if (auth == null) return "❌ Not logged in";
        return "✅ Logged in as: " + auth.getName() + "<br>Roles: " + auth.getAuthorities();
    }
}