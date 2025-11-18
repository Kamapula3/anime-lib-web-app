package ru.itis.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Integer id;
    private String username;
    private String email;
    private String regDate;
    private String role;
}
