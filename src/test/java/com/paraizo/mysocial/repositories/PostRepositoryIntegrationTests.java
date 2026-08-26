package com.paraizo.mysocial.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paraizo.mysocial.TestDataUtil;
import com.paraizo.mysocial.model.Post;
import com.paraizo.mysocial.model.User;
import com.paraizo.mysocial.respositories.PostRepository;
import com.paraizo.mysocial.respositories.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PostRepositoryIntegrationTests {

    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public PostRepositoryIntegrationTests(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @BeforeEach
    public void setUp(){

        // Delete first the post (which is binded to the user)
        postRepository.deleteAll();

        // then, delete the user
        userRepository.deleteAll();
    }

    @Test
    public void testThatPostsCanBeCreatedAndRetrieved(){
        User user = TestDataUtil.createUserA();

        User savedUser = userRepository.save(user);

        Post post = TestDataUtil.createPostA(savedUser);

        Post savedPost = postRepository.save(post);
        Optional<Post> retrievedPost = postRepository.findById(savedPost.getId());

        assertThat(retrievedPost).isPresent();
        assertThat(retrievedPost.get()).isEqualTo(savedPost);
    }

    @Test
    public void testThatMultiplePostsCanBeCreatedAndRetrieved(){
        User userA = TestDataUtil.createUserA();
        User userB = TestDataUtil.createUserB();
        User userC = TestDataUtil.createUserC();

        User saveduserA = userRepository.save(userA);
        User saveduserB = userRepository.save(userB);
        User saveduserC = userRepository.save(userC);

        Post postA = TestDataUtil.createPostA(saveduserA);
        Post postB = TestDataUtil.createPostA(saveduserB);
        Post postC = TestDataUtil.createPostA(saveduserC);

        Post savedA = postRepository.save(postA);
        Post savedB = postRepository.save(postB);
        Post savedC = postRepository.save(postC);

        Iterable<Post> retrievedPosts = postRepository.findAll();

        assertThat(retrievedPosts)
        .hasSize(3)
        .containsExactly(savedA, savedB, savedC);
    }

    @Test
    public void testThatPostsCanBeUpdated(){
        User user = TestDataUtil.createUserA();

        User savedUser = userRepository.save(user);

        Post post = TestDataUtil.createPostA(savedUser);

        Post savedPost = postRepository.save(post);
        savedPost.setTitle("UPDATED");
        postRepository.save(savedPost);

        Optional<Post> result = postRepository.findById(savedPost.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedPost);
    }

    @Test
    public void testThatPostsCanBeDeleted(){
        User user = TestDataUtil.createUserA();

        User savedUser = userRepository.save(user);

        Post post = TestDataUtil.createPostA(savedUser);

        Post savedPost = postRepository.save(post);

        Optional<Post> result = postRepository.findById(savedPost.getId());

        postRepository.delete(savedPost);

        Optional<Post> emptyResult = postRepository.findById(savedPost.getId());

        assertThat(result).isPresent();
        assertThat(emptyResult).isEmpty();
    }
    
}
