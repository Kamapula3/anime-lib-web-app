package ru.itis.repositories.comment;

import ru.itis.models.Comment;
import ru.itis.repositories.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment> {
    Comment getCommentById(Integer animeId, Integer userId);

    List<Comment> findAllByAnimeId(Integer commentId);

    void removeById(Integer commentId, Integer userId, Integer animeId);

    String getCommentatorName(Integer userId);
}
