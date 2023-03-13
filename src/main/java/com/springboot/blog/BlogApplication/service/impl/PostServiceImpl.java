package com.springboot.blog.BlogApplication.service.impl;

import com.springboot.blog.BlogApplication.entity.Post;
import com.springboot.blog.BlogApplication.exception.ResourceNotFoundException;
import com.springboot.blog.BlogApplication.payload.CommentDto;
import com.springboot.blog.BlogApplication.payload.PostDto;
import com.springboot.blog.BlogApplication.payload.PostResponse;
import com.springboot.blog.BlogApplication.repository.PostRepository;
import com.springboot.blog.BlogApplication.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    private PostRepository postRepository;

    private ModelMapper mapper;

    @Override
    public PostDto createPost(PostDto postDto) {

        //Conversion of DTO to Entity
        Post post = mapToEntity(postDto);
//        Post post = new Post();
//        post.setId(postDto.getId());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
//        post.setTitle(postDto.getTitle());

        //postRepository.save() method is used to store data into DB
        // and converting Entity to DTO to send response to RestApi using PostDto
        Post newPost = postRepository.save(post);

        //Convert Entity to DTO
        PostDto postResponse= mapToDto(newPost);
//        PostDto postResponse= new PostDto();
//        postResponse.setId(newPost.getId());
//        postResponse.setContent(newPost.getContent());
//        postResponse.setTitle(newPost.getTitle());
//        postResponse.setDescription(newPost.getDescription());

        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> posts = postRepository.findAll(pageable);

        // get content for page object
        List<Post> listOfPosts = posts.getContent();

        List<PostDto> content =  listOfPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;

    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDto(post);
    }

    public PostDto updatePostById(long id, PostDto postDto){
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        //post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        Post updatedPost =  postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }


    //Converted Entity into DTO
    private PostDto mapToDto(Post post){
//        PostDto postDto = new PostDto();
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());

        PostDto postDto =  mapper.map(post, PostDto.class);

        return postDto;
    }

    //Converted DTO to Entity
    private Post mapToEntity(PostDto postDto){
//        Post post = new Post();
//        post.setId(postDto.getId());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
//        post.setTitle(postDto.getTitle());

        Post post = mapper.map(postDto, Post.class);

        return post;
    }

}
