package com.api.helpdesk.service;

import com.api.helpdesk.dto.UserDTO;
import com.api.helpdesk.entity.Users;
import com.api.helpdesk.exception.NotFoundDBException;
import com.api.helpdesk.exception.SoftDeleteException;
import com.api.helpdesk.exception.UserAlreadyExistsException;
import com.api.helpdesk.mapper.UserMapper;
import com.api.helpdesk.repository.TicketRepository;
import com.api.helpdesk.repository.UserRepository;
import com.api.helpdesk.utils.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;
    private Users users;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        users = new Users();
    }

    @Test
    void whenRegisterUser_thenUserIsRegistered() {
        userDTO.setEmail("email@email.com");
        userDTO.setName("name");

        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);

        Users usersEntity = new Users();
        when(userMapper.toEntity(userDTO)).thenReturn(usersEntity);

        Users savedUser = new Users();
        savedUser.setId(1L);
        savedUser.setName(userDTO.getName());
        savedUser.setEmail(userDTO.getEmail());
        savedUser.setDeleted(false);

        when(userRepository.save(usersEntity)).thenReturn(savedUser);

        when(userMapper.toDTO(savedUser)).thenReturn(new UserDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.isDeleted()));

        UserDTO result = userService.register(userDTO);

        verify(userRepository).save(usersEntity);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(userDTO.getName());
        assertThat(result.getEmail()).isEqualTo(userDTO.getEmail());
    }

    @Test
    void whenRegisterUser_withExistingEmail_thenReturnEmailAlreadyExists() {
        userDTO.setEmail("email@email.com");
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        UserAlreadyExistsException thrown = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.register(userDTO);
        });
        assertEquals("Usuário já cadastrado.", thrown.getMessage());

        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    void whenGetAllUsers_thenReturnAllUsers() {
        users.setId(1L);
        users.setName("Test User");
        users.setEmail("test@example.com");

        List<Users> usersList = List.of(users);
        Page<Users> usersPage = new PageImpl<>(usersList);
        when(userRepository.findAllActiveUsers(any(Pageable.class))).thenReturn(usersPage);
        when(userMapper.toDTO(any(Users.class))).thenReturn(userDTO);

        Page<UserDTO> result = userService.getAllUsers(Pageable.unpaged());

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(userDTO);
        verify(userRepository).findAllActiveUsers(any(Pageable.class));
        verify(userMapper).toDTO(users);
    }

    @Test
    void whenGetUserById_thenReturnUser() throws NotFoundDBException {
        Long id = 1L;
        users.setId(id);
        when(userRepository.findActiveUserById(id)).thenReturn(Optional.of(users));
        when(userMapper.toDTO(users)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(id);

        assertThat(result).isEqualTo(userDTO);
        verify(userRepository).findActiveUserById(id);
    }

    @Test
    void whenGetUserById_withNonExistentId_thenReturnNotFound() {
        Long id = 1L;
        when(userRepository.findActiveUserById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundDBException.class, () -> userService.getUserById(id));
        verify(userRepository).findActiveUserById(id);
    }

    @Test
    void whenDeleteUserById_withNonExistentUser_thenReturnNotFound() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundDBException.class, () -> userService.deleteUserById(id));
        verify(userRepository).findById(id);
    }

    @Test
    void whenDeleteUserById_withOpenTickets_thenReturnCannotDelete() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(users));

        when(ticketRepository.countTicketsByCustomerIdAndNotConcluded(id, TicketStatus.CONCLUIDO)).thenReturn(1L);

        assertThrows(SoftDeleteException.class, () -> userService.deleteUserById(id));
        verify(userRepository).findById(id);
        verify(ticketRepository).countTicketsByCustomerIdAndNotConcluded(id, TicketStatus.CONCLUIDO);
    }

    @Test
    void whenDeleteUserById_withExistingId_thenUserIsDeletedSuccessfully() throws NotFoundDBException {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(users));

        when(ticketRepository.countTicketsByCustomerIdAndNotConcluded(id, TicketStatus.CONCLUIDO)).thenReturn(0L);

        userService.deleteUserById(id);

        verify(userRepository).softDeleteUserById(id);
    }
}
