package ru.itis.services;

import ru.itis.dto.CommentDto;

import java.util.List;

public interface CommentService {
    void createComment(Integer userId, Integer animeId, String text);

    void removeComment(Integer commentId, Integer userId, Integer animeId);

    List<CommentDto> getAllByAnimeId(Integer animeId);
}
