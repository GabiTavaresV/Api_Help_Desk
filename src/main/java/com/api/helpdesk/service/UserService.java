package com.api.helpdesk.service;

import com.api.helpdesk.exception.NotFoundDBException;
import com.api.helpdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api.helpdesk.entity.Users;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Users register(Users users) {
        System.out.println("Recebido no service: " + users);
        return userRepository.save(users);
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users getUserById(Long id) throws NotFoundDBException {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundDBException("Usuário não encontrado!"));
    }
}
