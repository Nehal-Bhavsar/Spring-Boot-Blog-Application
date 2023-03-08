package com.springboot.blog.BlogApplication.repository;

import com.springboot.blog.BlogApplication.entity.Comment;
import com.springboot.blog.BlogApplication.payload.CommentDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(long postId);

}
