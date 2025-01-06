package com.api.helpdesk.mapper;

import org.mapstruct.Mapper;

import com.api.helpdesk.dto.UserDTO;
import com.api.helpdesk.entity.Users;

@Mapper(componentModel = "spring")
public interface UserMapper {

    default UserDTO toDTO(Users user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .isDeleted(user.isDeleted())
                .build();
    }

    Users toEntity(UserDTO userDTO);
}
