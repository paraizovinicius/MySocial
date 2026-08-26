package com.paraizo.mysocial.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.paraizo.mysocial.TestDataUtil;
import com.paraizo.mysocial.model.Post;
import com.paraizo.mysocial.model.PostLike;
import com.paraizo.mysocial.model.User;
import com.paraizo.mysocial.respositories.PostLikeRepository;
import com.paraizo.mysocial.respositories.PostRepository;
import com.paraizo.mysocial.respositories.UserRepository;

// each test runs in its own transaction that's rolled back afterwards, so no manual
// cleanup or delete-ordering is needed - nothing a test creates is ever actually committed
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class PostLikeRepositoryIntegrationTests {

    private PostLikeRepository postLikeRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public PostLikeRepositoryIntegrationTests(PostLikeRepository postLikeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Test
    public void testThatLikeCanBeCreatedAndRetrieved(){
        User user = TestDataUtil.createUserA();
        User savedUser = userRepository.save(user);
        Post post = TestDataUtil.createPostA(savedUser);
        Post savedPost = postRepository.save(post);
        PostLike postLike = TestDataUtil.createPostLike(savedUser, savedPost);
        PostLike savedPostLike = postLikeRepository.save(postLike);

        Optional<PostLike> result = postLikeRepository.findById(savedPostLike.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedPostLike);
    }

    @Test
    public void testThatMultipleLikesCanBeCreatedAndRetrieved(){
        User user = TestDataUtil.createUserA();
        User savedUser = userRepository.save(user);

        Post postA = TestDataUtil.createPostA(savedUser);
        Post savedA = postRepository.save(postA);

        Post postB = TestDataUtil.createPostB(savedUser);
        Post savedB = postRepository.save(postB);

        Post postC = TestDataUtil.createPostC(savedUser);
        Post savedC = postRepository.save(postC);

        PostLike postLikeA = TestDataUtil.createPostLike(savedUser, savedA);
        PostLike savedPostLikeA = postLikeRepository.save(postLikeA);

        PostLike postLikeB = TestDataUtil.createPostLike(savedUser, savedB);
        PostLike savedPostLikeB = postLikeRepository.save(postLikeB);

        PostLike postLikeC = TestDataUtil.createPostLike(savedUser, savedC);
        PostLike savedPostLikeC = postLikeRepository.save(postLikeC);

        Iterable<PostLike> result = postLikeRepository.findAll();

        assertThat(result)
        .hasSize(3)
        .containsExactly(savedPostLikeA, savedPostLikeB, savedPostLikeC);
    }

    @Test
    public void testThatLikeCanBeDeleted(){
        //when a like is deleted, that is a simple unlike operation

        User user = TestDataUtil.createUserA();
        User savedUser = userRepository.save(user);
        Post post = TestDataUtil.createPostA(savedUser);
        Post savedPost = postRepository.save(post);
        PostLike postLike = TestDataUtil.createPostLike(savedUser, savedPost);

        PostLike savedPostLike = postLikeRepository.save(postLike);
        Optional<PostLike> result = postLikeRepository.findById(savedPostLike.getId());

        postLikeRepository.delete(savedPostLike);
        Optional<PostLike> emptyResult = postLikeRepository.findById(savedPostLike.getId());

        assertThat(result).isPresent();
        assertThat(emptyResult).isEmpty();
    }
}
