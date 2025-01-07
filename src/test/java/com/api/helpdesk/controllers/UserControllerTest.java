package com.api.helpdesk.controllers;

import com.api.helpdesk.controller.UserController;
import com.api.helpdesk.dto.UserDTO;
import com.api.helpdesk.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


public class UserControllerTest {

    private UserController userController;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void whenPostUser_thenCreateUser() {
        UserDTO user = new UserDTO();
        user.setName("John");
        user.setEmail("john@gmail.com");

        when(userService.register(ArgumentMatchers.any(UserDTO.class)))
                .thenReturn(user);

        ResponseEntity<UserDTO> response = userController.createUser(user);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("John");

        verify(userService).register(any(UserDTO.class));
    }

    @Test
    void whenGetUsers_thenListAllUsers() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        UserDTO user1 = new UserDTO();
        user1.setName("John");
        UserDTO user2 = new UserDTO();
        user2.setName("Jane");

        Page<UserDTO> page = new PageImpl<>(Arrays.asList(user1, user2), pageable, 2);

        when(userService.getAllUsers(pageable)).thenReturn(page);

        ResponseEntity<Page<UserDTO>> response = userController.getAll(pageable);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(2);
        assertThat(response.getBody().getContent()).hasSize(2);
        assertThat(response.getBody().getContent().get(0).getName()).isEqualTo("John");
        assertThat(response.getBody().getContent().get(1).getName()).isEqualTo("Jane");

        verify(userService).getAllUsers(pageable);
    }

    @Test
    void whenGetUserById_thenReturnUser() {
        Long userId = 1L;
        UserDTO user = new UserDTO();
        user.setName("John");

        when(userService.getUserById(userId)).thenReturn(user);

        ResponseEntity<UserDTO> response = userController.getById(userId);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("John");

        verify(userService).getUserById(userId);
    }

    @Test
    void whenDeleteUserById_thenUserIsDeleted() {
        Long userId = 1L;

        ResponseEntity<Void> response = userController.delete(userId);

        assertThat(response.getStatusCode().value()).isEqualTo(204);

        verify(userService).deleteUserById(userId);
    }
}
