package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.dto.SignUpRequest;
import com.stackclone.stackoverflow_clone.entity.User;
import com.stackclone.stackoverflow_clone.enums.UserRole;
import com.stackclone.stackoverflow_clone.repository.UserRepository;
import com.stackclone.stackoverflow_clone.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final  UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUpRequest(SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            throw new RuntimeException("User allready registered");
        }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(UserRole.USER);
        user.setUrl("https://res.cloudinary.com/dx9zbuld9/image/upload/v1753328661/bwto7aeew9b6wex8wmuv.jpg");

        userRepository.save(user);
    }
}
