package com.paraizo.mysocial.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paraizo.mysocial.TestDataUtil;
import com.paraizo.mysocial.model.Follow;
import com.paraizo.mysocial.model.User;
import com.paraizo.mysocial.respositories.FollowRepository;
import com.paraizo.mysocial.respositories.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class FollowRepositoryIntegrationTests {

    private UserRepository userRepository;
    private FollowRepository followRepository;

    @Autowired
    public FollowRepositoryIntegrationTests(UserRepository userRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    @Test
    public void testThatFollowCanBeCreatedAndRetrieved(){
        User userA = TestDataUtil.createUserA();
        User userB = TestDataUtil.createUserB();

        User savedUserA = userRepository.save(userA);
        User savedUserB = userRepository.save(userB);

        Follow follow = TestDataUtil.createFollow(savedUserA, savedUserB);
        Follow savedFollow = followRepository.save(follow);

        Optional<Follow> result = followRepository.findById(savedFollow.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedFollow);
    }

    @Test
    public void testThatMultipleFollowsCanBeCreatedAndRetrieved(){
        User userA = TestDataUtil.createUserA();
        User userB = TestDataUtil.createUserB();
        User userC = TestDataUtil.createUserC();
        User savedUserA = userRepository.save(userA);
        User savedUserB = userRepository.save(userB);
        User savedUserC = userRepository.save(userC);

        Follow followAB = TestDataUtil.createFollow(savedUserA, savedUserB);
        Follow followBA = TestDataUtil.createFollow(savedUserB, savedUserA);
        Follow followAC = TestDataUtil.createFollow(savedUserA, savedUserC);
        Follow followCB = TestDataUtil.createFollow(savedUserC, savedUserB);

        Follow savedFollowAB = followRepository.save(followAB);
        Follow savedFollowBA = followRepository.save(followBA);
        Follow savedFollowAC = followRepository.save(followAC);
        Follow savedFollowCB = followRepository.save(followCB);

        Iterable<Follow> results = followRepository.findAll();

        assertThat(results)
        .hasSize(4)
        .containsExactly(savedFollowAB, savedFollowBA, savedFollowAC, savedFollowCB);

    }

    @Test
    public void testThatFollowCanBeDeleted(){
        // An "Unfollow" is basically a deletion 
        User userA = TestDataUtil.createUserA();
        User userB = TestDataUtil.createUserB();

        User savedUserA = userRepository.save(userA);
        User savedUserB = userRepository.save(userB);

        Follow follow = TestDataUtil.createFollow(savedUserA, savedUserB);
        Follow savedFollow = followRepository.save(follow);

        Optional<Follow> result = followRepository.findById(savedFollow.getId());

        followRepository.delete(savedFollow);

        Optional<Follow> EmptyResult = followRepository.findById(savedFollow.getId());

        assertThat(result).isPresent();
        assertThat(EmptyResult).isEmpty();

    }  


}
