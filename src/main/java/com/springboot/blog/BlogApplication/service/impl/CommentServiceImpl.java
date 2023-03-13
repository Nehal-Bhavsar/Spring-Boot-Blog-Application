package com.springboot.blog.BlogApplication.service.impl;

import com.springboot.blog.BlogApplication.entity.Comment;
import com.springboot.blog.BlogApplication.entity.Post;
import com.springboot.blog.BlogApplication.exception.BlogApiException;
import com.springboot.blog.BlogApplication.exception.ResourceNotFoundException;
import com.springboot.blog.BlogApplication.payload.CommentDto;
import com.springboot.blog.BlogApplication.repository.CommentRepository;
import com.springboot.blog.BlogApplication.repository.PostRepository;
import com.springboot.blog.BlogApplication.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    private PostRepository postRepository;

    private ModelMapper mapper;
    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository,
                              ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment =  mapToEntity(commentDto);
        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id", postId));

        // set post to comment entity
        comment.setPost(post);

        // save comment entity to DB
        Comment newComment = commentRepository.save(comment);
        return mapToDto(newComment);
    }

    private Comment mapToEntity(CommentDto commentDto){
//        Comment comment = new Comment();
//        comment.setId(commentDto.getId());
//        comment.setBody(commentDto.getBody());
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
        Comment comment = mapper.map(commentDto, Comment.class);
        return comment;
    }

    private CommentDto mapToDto(Comment comment){
//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setBody(comment.getBody());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());

        CommentDto commentDto = mapper.map(comment, CommentDto.class);
        return commentDto;
    }

    public List<CommentDto> getCommentsByPostId(long postId){

        //retrieve comments by postId
        List<Comment> comments = commentRepository.findByPostId(postId);

        //convert the list of comment entity to list of comment's dto
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());

    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id", postId));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","Id", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post");
        }

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post", "Id", postId)
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "Id", commentId)
        );

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }

        comment.setName(commentRequest.getName());
        comment.setBody(commentRequest.getBody());
        comment.setEmail(commentRequest.getEmail());

        Comment updatedComment = commentRepository.save(comment);
        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post","Id", postId)
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new ResourceNotFoundException("Comment","Id", commentId)
        );

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }

        commentRepository.delete(comment);
    }


}
