package ru.itis.services.impl;

import ru.itis.dto.UserDto;
import ru.itis.models.User;
import ru.itis.repositories.user.UserRepository;
import ru.itis.services.ProfileService;

public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;

    public ProfileServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDto getUserProfileInfo(String email){
        User userFromRepository = userRepository.findByEmail(email);

        Integer id = userFromRepository.getId();
        String username = userFromRepository.getUserName();
        String login = userFromRepository.getEmail();
        String regDate = userFromRepository.getRegDate().split(" ")[0];


        return UserDto.builder()
                .id(id)
                .username(username)
                .email(login)
                .regDate(regDate)
                .build();
    }
}
