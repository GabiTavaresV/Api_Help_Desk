package com.api.helpdesk.mapper;

import com.api.helpdesk.dto.UserDTO;
import com.api.helpdesk.entity.Users;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void whenConvertToDTO_thenReturnDTO() {
        Users user = new Users();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("johndoe@example.com");
        user.setDeleted(false);

        UserDTO result = userMapper.toDTO(user);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getName()).isEqualTo(user.getName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getIsDeleted()).isEqualTo(user.isDeleted());
    }

    @Test
    void whenConvertToDTO_withNull_thenReturnNull() {
        UserDTO result = userMapper.toDTO(null);

        assertThat(result).isNull();
    }

    @Test
    void whenConvertToEntity_thenReturnEntity() {
        UserDTO userDTO = UserDTO.builder()
                .id(1L)
                .name("John Doe")
                .email("johndoe@example.com")
                .isDeleted(false)
                .build();

        Users result = userMapper.toEntity(userDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userDTO.getId());
        assertThat(result.getName()).isEqualTo(userDTO.getName());
        assertThat(result.getEmail()).isEqualTo(userDTO.getEmail());
    }
}
