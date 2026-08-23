package com.paraizo.mysocial.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paraizo.mysocial.respositories.UserRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserRepositoryIntegrationTests {

    private UserRepository userRepository;

    @Autowired
    public UserRepositoryIntegrationTests(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // @Test
    // public void testThatUsersCanBeCreatedAndListed(){

    // }
}
