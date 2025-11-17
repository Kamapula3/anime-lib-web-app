package ru.itis.services;

import ru.itis.dto.SignInForm;
import ru.itis.dto.SignUpForm;
import ru.itis.models.User;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public interface AuthService {
    Boolean signUp(SignUpForm signUpForm, HttpServletRequest req);

    Boolean signIn(SignInForm signInForm, HttpServletRequest req);
}
