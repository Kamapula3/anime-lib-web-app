package ru.itis.services;

import ru.itis.dto.UserDto;

public interface ProfileService {
    UserDto getUserProfileInfo(String email);
}
