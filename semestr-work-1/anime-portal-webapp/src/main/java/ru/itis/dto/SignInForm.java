package ru.itis.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInForm {
    private String login;
    private String password;
}
