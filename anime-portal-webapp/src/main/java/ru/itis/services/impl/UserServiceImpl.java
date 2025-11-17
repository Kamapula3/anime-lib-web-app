package ru.itis.services.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.dto.SignUpForm;
import ru.itis.dto.UserDto;
import ru.itis.exceptions.DatabaseException;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.models.User;
import ru.itis.repositories.user.UserRepository;
import ru.itis.services.UserService;
import ru.itis.utils.EmailValidator;
import ru.itis.utils.PasswordValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private static final String USER_IS_FOUND_ERROR = "Пользователся с такой электронной почтой уже существует";

    private static final String INVALID_FORMAT_OF_EMAIL_ERROR = "Неверный формат электронной почты";

    private static final String INVALID_FORMAT_OF_PASSWORD_ERROR = "Пароль должен содержать минимум одну цифру и одну букву";

    private static final String INVALID_LENGTH_OF_PASSWORD_ERROR = "Пароль должен содержать не менее 8 символов";

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public List<UserDto> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            List<UserDto> newUsers = new ArrayList<>();

            for (User user : users){
                UserDto userDto = UserDto.builder()
                        .id(user.getId())
                        .username(user.getUserName())
                        .email(user.getEmail())
                        .regDate(user.getRegDate().split("\\.")[0])
                        .role(userRepository.getUserRole(user.getRoleId()))
                        .build();
                newUsers.add(userDto);
            }

            return newUsers;

        } catch (DatabaseException e){
            throw new DatabaseException("не удаслось найти пользователей");
        }
    }

    public Boolean addUser(SignUpForm signUpForm, HttpServletRequest req){
        Boolean isSignUp = false;

        User user;
        String username = signUpForm.getUsername();
        String email = signUpForm.getEmail();
        String password = signUpForm.getPassword();
        Integer roleId = signUpForm.getRoleId();

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

        User currentUser;

        try {
            currentUser = userRepository.findByEmail(email);
        } catch (EntityNotFoundException e){
            currentUser = null;
        }


        if(currentUser != null){
            req.setAttribute("error", USER_IS_FOUND_ERROR);
            return isSignUp;
        }

        user = User.builder()
                .userName(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .roleId(roleId)
                .build();
        userRepository.saveWithRole(user);

        return !isSignUp;
    }
}
