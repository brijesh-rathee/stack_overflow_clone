package com.stackclone.stackoverflow_clone.controller;

import com.stackclone.stackoverflow_clone.dto.SignUpRequest;
import com.stackclone.stackoverflow_clone.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login-page";
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("signUpRequest", new SignUpRequest());
        return "signup-page";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute SignUpRequest signUpRequest, Model model) {
        try {
            authService.signUpRequest(signUpRequest);
            return "redirect:/auth/login";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("signUpRequest", signUpRequest);
            return "signup-page";
        }
    }
}
