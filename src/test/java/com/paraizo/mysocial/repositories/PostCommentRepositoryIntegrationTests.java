package com.paraizo.mysocial.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paraizo.mysocial.TestDataUtil;
import com.paraizo.mysocial.model.Post;
import com.paraizo.mysocial.model.PostComment;
import com.paraizo.mysocial.model.User;
import com.paraizo.mysocial.respositories.PostCommentRepository;
import com.paraizo.mysocial.respositories.PostRepository;
import com.paraizo.mysocial.respositories.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class PostCommentRepositoryIntegrationTests {

    private PostCommentRepository postCommentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public PostCommentRepositoryIntegrationTests(PostCommentRepository postCommentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.postCommentRepository = postCommentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Test
    public void testThatPostCommentCanBeCreatedAndRetrieved(){
        User user = TestDataUtil.createUserA();
        User savedUser = userRepository.save(user);
        Post post = TestDataUtil.createPostA(savedUser);
        Post savedPost = postRepository.save(post);
        PostComment postComment = TestDataUtil.createPostCommentA(savedUser, savedPost);
        PostComment savedComment = postCommentRepository.save(postComment);

        Optional<PostComment> result = postCommentRepository.findById(savedComment.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedComment);
    }

    @Test
    public void testThatMultiplePostCommentCanBeCreatedAndRetrieved(){
        User user = TestDataUtil.createUserA();
        User savedUser = userRepository.save(user);
        Post post = TestDataUtil.createPostA(savedUser);
        Post savedPost = postRepository.save(post);
        PostComment postCommentA = TestDataUtil.createPostCommentA(savedUser, savedPost);
        PostComment postCommentB = TestDataUtil.createPostCommentB(savedUser, savedPost);

        postCommentRepository.save(postCommentA);
        postCommentRepository.save(postCommentB);
        
        Iterable<PostComment> results = postCommentRepository.findAll();

        assertThat(results)
        .hasSize(2)
        .containsExactly(postCommentA, postCommentB);
           
    }

    @Test
    public void testThatPostCommentCanBeUpdated(){
        User user = TestDataUtil.createUserA();
        User savedUser = userRepository.save(user);
        Post post = TestDataUtil.createPostA(savedUser);
        Post savedPost = postRepository.save(post);

        PostComment postComment = TestDataUtil.createPostCommentA(savedUser, savedPost);

        PostComment savedComment = postCommentRepository.save(postComment);

        savedComment.setContent("UPDATED");

        postCommentRepository.save(postComment);

        Optional<PostComment> changedResult = postCommentRepository.findById(savedComment.getId());

        assertThat(changedResult).isPresent();
        assertThat(changedResult.get()).isEqualTo(savedComment);
    }

    @Test
    public void testThatPostCommentCanBeDeleted(){
        User user = TestDataUtil.createUserA();
        User savedUser = userRepository.save(user);
        Post post = TestDataUtil.createPostA(savedUser);
        Post savedPost = postRepository.save(post);

        PostComment postComment = TestDataUtil.createPostCommentA(savedUser, savedPost);

        PostComment savedComment = postCommentRepository.save(postComment);

        Optional<PostComment> result = postCommentRepository.findById(savedComment.getId());

        postCommentRepository.delete(savedComment);

        Optional<PostComment> resultEmpty = postCommentRepository.findById(savedComment.getId());

        assertThat(result).isPresent();
        assertThat(resultEmpty).isEmpty();

    }
    
}
