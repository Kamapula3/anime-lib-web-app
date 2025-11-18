package ru.itis.services;

import ru.itis.dto.SignUpForm;
import ru.itis.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    Boolean addUser(SignUpForm signUpForm, HttpServletRequest req);
}
