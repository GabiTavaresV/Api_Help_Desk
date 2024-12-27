package com.api.helpdesk.repository;


import com.api.helpdesk.entity.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private Users user;

    @BeforeEach
    public void setUp() {
        user = new Users();
        user.setName("Test");
        user.setEmail("test@example.com");
        user.setDeleted(false);
        entityManager.persistAndFlush(user);
    }

    @Test
    public void whenFindAllActiveUsers_thenReturnActiveUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Users> activeUsers = userRepository.findAllActiveUsers(pageable);
         assertThat(activeUsers.hasContent()).isTrue();
        assertTrue(activeUsers.getContent().contains(user));
        Assertions.assertThat(activeUsers.getTotalElements()).isGreaterThan(0);
        Assertions.assertThat(activeUsers.getSize()).isLessThanOrEqualTo(10);
    }

    @Test
    public void whenFindActiveUserById_thenReturnActiveUser() {
        Optional<Users> foundUser = userRepository.findActiveUserById(user.getId());
        assertTrue(foundUser.isPresent());
        assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void whenSoftDeleteUserById_thenUserIsDeleted() {
        userRepository.softDeleteUserById(user.getId());
        Optional<Users> deletedUser = userRepository.findActiveUserById(user.getId());
        assertTrue(deletedUser.isEmpty());
    }

    @Test
    public void whenCheckForExistingEmail_thenReturnTrue() {
        boolean exists = userRepository.existsByEmail(user.getEmail());
        assertTrue(exists);
    }

    @Test
    public void whenCheckForNonExistingEmail_thenReturnFalse() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");
        assertThat(exists).isFalse();
    }
}
