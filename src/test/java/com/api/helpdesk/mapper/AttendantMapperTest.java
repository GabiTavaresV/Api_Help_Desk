package com.api.helpdesk.mapper;

import com.api.helpdesk.dto.AttendantDTO;
import com.api.helpdesk.entity.Attendant;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class AttendantMapperTest {

    private final AttendantMapper attendantMapper = Mappers.getMapper(AttendantMapper.class);

    @Test
    void whenConvertAttendantToAttendantDTO_thenReturnAttendantDTO() {
        Attendant attendant = new Attendant();
        attendant.setId(1L);
        attendant.setName("John Doe");

        AttendantDTO result = attendantMapper.attendantToAttendantDTO(attendant);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(attendant.getId());
        assertThat(result.getName()).isEqualTo(attendant.getName());
        assertThat(result.getIsDeleted()).isFalse();
    }

    @Test
    void whenConvertAttendantToAttendantDTO_withNullAttendant_thenReturnNull() {
        AttendantDTO result = attendantMapper.attendantToAttendantDTO(null);

        assertThat(result).isNull();
    }

    @Test
    void whenConvertAttendantDTOToAttendant_thenReturnAttendant() {
        AttendantDTO attendantDTO = AttendantDTO.builder()
                .id(1L)
                .name("John Doe")
                .isDeleted(false)
                .build();

        Attendant result = attendantMapper.attendantDTOToAttendant(attendantDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(attendantDTO.getId());
        assertThat(result.getName()).isEqualTo(attendantDTO.getName());
    }
}
