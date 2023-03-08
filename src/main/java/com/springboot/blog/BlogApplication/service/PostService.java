package com.springboot.blog.BlogApplication.service;

import com.springboot.blog.BlogApplication.entity.Comment;
import com.springboot.blog.BlogApplication.entity.Post;
import com.springboot.blog.BlogApplication.payload.CommentDto;
import com.springboot.blog.BlogApplication.payload.PostDto;
import com.springboot.blog.BlogApplication.payload.PostResponse;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto getPostById(long id);

    PostDto updatePostById(long id, PostDto postDto);

    void deletePostById(long id);


}
