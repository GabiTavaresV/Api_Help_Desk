package com.api.helpdesk.service;

import com.api.helpdesk.dto.UserDTO;
import com.api.helpdesk.exception.NotFoundDBException;
import com.api.helpdesk.mapper.UserMapper;
import com.api.helpdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api.helpdesk.entity.Users;
import java.util.List;
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
        Users users = userMapper.toEntity(userDTO);
        Users savedUsers = userRepository.save(users);
        return userMapper.toDTO(savedUsers);
    }

    public List<UserDTO> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) throws NotFoundDBException {
        Users users = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundDBException("Usuário não encontrado!"));
        return userMapper.toDTO(users);
    }
}
