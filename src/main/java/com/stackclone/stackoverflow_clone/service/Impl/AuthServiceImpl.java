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
    private static final String DEFAULT_IMAGE_URL =
            "https://res.cloudinary.com/dx9zbuld9/image/upload/v1753347401/l9xivdngn5jjf33glmwo.jpg";

    private final  UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUpRequest(SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            throw new RuntimeException("User already registered");
        }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(UserRole.USER);
        user.setUrl(DEFAULT_IMAGE_URL);

        userRepository.save(user);
    }
}