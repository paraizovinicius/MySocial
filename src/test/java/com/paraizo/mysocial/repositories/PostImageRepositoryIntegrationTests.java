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
import com.paraizo.mysocial.model.PostImage;
import com.paraizo.mysocial.model.User;
import com.paraizo.mysocial.respositories.PostImageRepository;
import com.paraizo.mysocial.respositories.PostRepository;
import com.paraizo.mysocial.respositories.UserRepository;

// Only exercises the Postgres-backed metadata (storageKey/position) - no bytes are read or
// written, so these tests never touch the real R2 bucket or its free-tier quota.
// each test runs in its own transaction that's rolled back afterwards, so no manual
// cleanup or delete-ordering is needed - nothing a test creates is ever actually committed
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class PostImageRepositoryIntegrationTests {

    private PostImageRepository postImageRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public PostImageRepositoryIntegrationTests(PostImageRepository postImageRepository, PostRepository postRepository, UserRepository userRepository) {
        this.postImageRepository = postImageRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Test
    public void testThatImageCanBeCreatedAndRetrieved(){
        User savedUser = userRepository.save(TestDataUtil.createUserA());
        Post savedPost = postRepository.save(TestDataUtil.createPostA(savedUser));
        PostImage image = TestDataUtil.createImageA(savedPost);

        PostImage savedImage = postImageRepository.save(image);
        Optional<PostImage> result = postImageRepository.findById(savedImage.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedImage);
    }

    @Test
    public void testThatMultipleImagesCanBeCreatedAndRetrievedInOrder(){
        User savedUser = userRepository.save(TestDataUtil.createUserA());
        Post savedPost = postRepository.save(TestDataUtil.createPostA(savedUser));

        PostImage savedImageA = postImageRepository.save(TestDataUtil.createImageA(savedPost));
        PostImage savedImageB = postImageRepository.save(TestDataUtil.createImageB(savedPost));
        PostImage savedImageC = postImageRepository.save(TestDataUtil.createImageC(savedPost));

        Iterable<PostImage> result = postImageRepository.findAll();

        assertThat(result)
        .hasSize(3)
        .containsExactlyInAnyOrder(savedImageA, savedImageB, savedImageC);
    }

    @Test
    public void testThatImageCanBeUpdated(){
        User savedUser = userRepository.save(TestDataUtil.createUserA());
        Post savedPost = postRepository.save(TestDataUtil.createPostA(savedUser));
        PostImage savedImage = postImageRepository.save(TestDataUtil.createImageA(savedPost));

        savedImage.setPosition(5);
        postImageRepository.save(savedImage);

        Optional<PostImage> result = postImageRepository.findById(savedImage.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getPosition()).isEqualTo(5);
    }

    @Test
    public void testThatImageCanBeDeleted(){
        User savedUser = userRepository.save(TestDataUtil.createUserA());
        Post savedPost = postRepository.save(TestDataUtil.createPostA(savedUser));
        PostImage savedImage = postImageRepository.save(TestDataUtil.createImageA(savedPost));

        Optional<PostImage> result = postImageRepository.findById(savedImage.getId());

        postImageRepository.delete(savedImage);
        Optional<PostImage> emptyResult = postImageRepository.findById(savedImage.getId());

        assertThat(result).isPresent();
        assertThat(emptyResult).isEmpty();
    }
}
