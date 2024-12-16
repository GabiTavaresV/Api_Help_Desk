package com.api.helpdesk.mapper;

import com.api.helpdesk.dto.UserDTO;
import com.api.helpdesk.entity.Users;

public class UserMapper {

    public UserDTO toDTO(Users user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }



    public Users toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        Users users = new Users();
        users.setId(userDTO.getId());
        users.setName(userDTO.getName());
        users.setEmail(userDTO.getEmail());
        return users;
    }
}
