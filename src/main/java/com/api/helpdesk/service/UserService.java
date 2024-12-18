package com.api.helpdesk.service;

import com.api.helpdesk.controller.handler.EmailAlreadyExistsException;
import com.api.helpdesk.dto.UserDTO;
import com.api.helpdesk.exception.NotFoundDBException;
import com.api.helpdesk.mapper.UserMapper;
import com.api.helpdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api.helpdesk.entity.Users;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper = new UserMapper();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO register(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email já cadastrado.");
        }

        Users users = userMapper.toEntity(userDTO);
        Users savedUsers = userRepository.save(users);
        return userMapper.toDTO(savedUsers);
    }

    public List<UserDTO> getAllUsers() {
        List<Users> users = userRepository.findAllActiveUsers();
        return users.stream().map(userMapper::toDTO).collect(Collectors.toList());

    }

    public UserDTO getUserById(Long id) throws NotFoundDBException {
        Users users = userRepository.findActiveUserById(id)
                .orElseThrow(() -> new NotFoundDBException("Usuário não encontrado!"));
        return userMapper.toDTO(users);
    }

    public Void deleteUserById(Long id) throws NotFoundDBException {
        Optional<Users> deviceOptional = userRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            throw new NotFoundDBException("Usuário não encontrado!");
        }
        userRepository.softDeleteUserById(id);
        return null;
    }
}
