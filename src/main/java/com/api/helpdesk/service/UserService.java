package com.api.helpdesk.service;

import com.api.helpdesk.exception.EmailAlreadyExistsException;
import com.api.helpdesk.dto.UserDTO;
import com.api.helpdesk.exception.NotFoundDBException;
import com.api.helpdesk.mapper.UserMapper;
import com.api.helpdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.api.helpdesk.entity.Users;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;


    public UserDTO register(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Usuário já cadastrado.");
        }

        Users users = userMapper.toEntity(userDTO);
        Users savedUsers = userRepository.save(users);
        return userMapper.toDTO(savedUsers);
    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<Users> users = userRepository.findAllActiveUsers(pageable);
        return users.map(userMapper::toDTO);

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
