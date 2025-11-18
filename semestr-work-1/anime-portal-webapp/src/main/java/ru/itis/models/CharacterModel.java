package ru.itis.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CharacterModel {
    private Integer id;
    private String name;
    private String surname;
    private String description;
    private Integer anime_id;
}
