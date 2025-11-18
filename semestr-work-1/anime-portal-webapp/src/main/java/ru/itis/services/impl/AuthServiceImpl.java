package ru.itis.services.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.dto.SignInForm;
import ru.itis.dto.SignUpForm;
import ru.itis.models.User;
import ru.itis.repositories.user.UserRepository;
import ru.itis.services.AuthService;
import ru.itis.utils.EmailValidator;
import ru.itis.utils.PasswordValidator;

import javax.servlet.http.HttpServletRequest;

public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private static final String USER_NOT_FOUND_ERROR = "Пользователся с такой электронной почтой не существует";

    private static final String USER_IS_FOUND_ERROR = "Пользователся с такой электронной почтой уже существует";

    private static final String INVALID_LOGIN_OR_PASSWORD_ERROR = "Неверный логин или пароль";

    private static final String INVALID_FORMAT_OF_EMAIL_ERROR = "Неверный формат электронной почты";

    private static final String INVALID_FORMAT_OF_PASSWORD_ERROR = "Пароль должен содержать минимум одну цифру и одну букву";

    private static final String INVALID_LENGTH_OF_PASSWORD_ERROR = "Пароль должен содержать не менее 8 символов";

    public AuthServiceImpl(UserRepository userRepository){
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userRepository = userRepository;
    }

    @Override
    public Boolean signUp(SignUpForm signUpForm, HttpServletRequest req){
        Boolean isSignUp = false;

        User user;
        String username = signUpForm.getUsername();
        String email = signUpForm.getEmail();
        String password = signUpForm.getPassword();

        if(!EmailValidator.isValidEmail(email)) {
            req.setAttribute("error", INVALID_FORMAT_OF_EMAIL_ERROR);
            return isSignUp;
        }

        if (!PasswordValidator.isValidPasswordFormat(password)){
            req.setAttribute("error", INVALID_FORMAT_OF_PASSWORD_ERROR);
            return isSignUp;
        }

        if(!PasswordValidator.isValidPasswordLength(password)){
            req.setAttribute("error", INVALID_LENGTH_OF_PASSWORD_ERROR);
            return isSignUp;
        }

        User currentUser = userRepository.findByEmail(email);

        if(currentUser != null){
            req.setAttribute("error", USER_IS_FOUND_ERROR);
            return isSignUp;
        }

        user = User.builder()
                .userName(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(user);

        return !isSignUp;
    }

    @Override
    public Boolean signIn(SignInForm signInForm, HttpServletRequest req){
        Boolean isSignIn = false;

        String login = signInForm.getLogin();
        String password = signInForm.getPassword();

        if (!EmailValidator.isValidEmail(login)) {
            req.setAttribute("error", INVALID_FORMAT_OF_EMAIL_ERROR);
            return isSignIn;
        }

        User user = userRepository.findByEmail(login);
        if (user == null) {
            req.setAttribute("error", USER_NOT_FOUND_ERROR);
            return isSignIn;
        }

        if(!passwordEncoder.matches(password, user.getPassword()) && login.equals(user.getEmail())){
            req.setAttribute("error", INVALID_LOGIN_OR_PASSWORD_ERROR);
            return isSignIn;
        }

        return !isSignIn;
    }
}
