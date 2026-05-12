package com.Croquetas.controller;

import com.Croquetas.model.User;
import com.Croquetas.repository.UserRepository;
import com.Croquetas.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam(required = false) String fullName,
                               @RequestParam(required = false) String bio,
                               @RequestParam(required = false) Integer joinYear,
                               @RequestParam(required = false) MultipartFile profilePhoto) throws IOException {

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setBio(bio);
        user.setJoinYear(joinYear);
        user.setRole("ROLE_CHEF");
        user.setApproved(false);

        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            String photoPath = fileUploadService.saveProfilePhoto(profilePhoto);
            user.setPhotoUrl(photoPath);
        }

        userRepository.save(user);
        return "redirect:/login?registered";
    }
}