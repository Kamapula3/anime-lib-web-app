package ru.itis.services.impl;

import ru.itis.dto.CommentDto;
import ru.itis.models.Comment;
import ru.itis.repositories.comment.CommentRepository;
import ru.itis.services.CommentService;

import java.util.ArrayList;
import java.util.List;

public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    @Override
    public void createComment(Integer userId, Integer animeId, String text){
        Comment comment = Comment.builder()
                .userId(userId)
                .animeId(animeId)
                .text(text.trim())
                .build();
        commentRepository.save(comment);
    }

    @Override
    public void removeComment(Integer commentId, Integer userId, Integer animeId){
        commentRepository.removeById(commentId, userId, animeId);
    }

    @Override
    public List<CommentDto> getAllByAnimeId(Integer animeId){
        List<Comment> comments = commentRepository.findAllByAnimeId(animeId);
        List<CommentDto> newComments = new ArrayList<>();

        for (Comment comment: comments){
            CommentDto commentDto = CommentDto.builder()
                    .id(comment.getId())
                    .userId(comment.getUserId())
                    .username(commentRepository.getCommentatorName(comment.getUserId()))
                    .animeId(comment.getAnimeId())
                    .text(comment.getText())
                    .createdAt(comment.getCreatedAt().split("\\.")[0])
                    .build();

            newComments.add(commentDto);
        }

        return newComments;
    }
}
