package com.springboot.blog.BlogApplication.service;

import com.springboot.blog.BlogApplication.entity.Comment;
import com.springboot.blog.BlogApplication.payload.CommentDto;
import org.aspectj.bridge.ICommand;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long postId, CommentDto commentDto);

    List<CommentDto> getCommentsByPostId(long postId);

    CommentDto getCommentById(Long postId, Long Id);

    CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest);

    void deleteComment(Long postId, Long commentId);
}
