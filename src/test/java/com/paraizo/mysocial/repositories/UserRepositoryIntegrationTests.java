package com.paraizo.mysocial.repositories;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paraizo.mysocial.TestDataUtil;
import com.paraizo.mysocial.model.User;
import com.paraizo.mysocial.respositories.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserRepositoryIntegrationTests {

    private UserRepository userRepository;

    @Autowired
    public UserRepositoryIntegrationTests(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void testThatUsersCanBeCreatedAndListed(){
        User user = TestDataUtil.createUserA();

        User savedUser = userRepository.save(user);

        Optional<User> retrievedUser = userRepository.findById(savedUser.getId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get()).isEqualTo(savedUser);
    }

    @Test
    public void testThatMultipleUsersCanBeCreatedAndRecalled(){
        User userA = TestDataUtil.createUserA();
        User userB = TestDataUtil.createUserB();
        User userC = TestDataUtil.createUserC();

        User savedUserA = userRepository.save(userA);
        User savedUserB = userRepository.save(userB);
        User savedUserC = userRepository.save(userC);

        Iterable<User> users = userRepository.findAll();

        assertThat(users)
        .hasSize(3)
        .containsExactly(savedUserA, savedUserB, savedUserC);
    }

    @Test
    public void testThatUserCanBeUpdated(){
        User user = TestDataUtil.createUserA();

        User savedUser = userRepository.save(user);
        user.setName("UPDATED");
        User savedUser2 = userRepository.save(user);

        Optional<User> retrievedUser = userRepository.findById(savedUser.getId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get()).isEqualTo(savedUser2);
    }

    @Test
    public void testThatUsersCanBeCreatedAndDeleted(){
        User user = TestDataUtil.createUserA();
        User savedUser = userRepository.save(user);
        Optional<User> retrievedUser = userRepository.findById(savedUser.getId());
        userRepository.delete(savedUser);
        Optional<User> retrievedDeleted = userRepository.findById(savedUser.getId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedDeleted).isEmpty();
    }
}
