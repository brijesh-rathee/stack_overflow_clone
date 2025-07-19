package com.stackclone.stackoverflow_clone.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest {

    private String username;
    private String email;
    private String password;
}
