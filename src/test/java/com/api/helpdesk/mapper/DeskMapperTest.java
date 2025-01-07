package com.api.helpdesk.mapper;

import com.api.helpdesk.dto.DeskDTO;
import com.api.helpdesk.entity.Desk;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class DeskMapperTest {

    private final DeskMapper deskMapper = Mappers.getMapper(DeskMapper.class);

    @Test
    void whenConvertDeskToDeskDTO_thenReturnDeskDTO() {
        Desk desk = new Desk();
        desk.setId(1L);

        DeskDTO result = deskMapper.deskToDeskDTO(desk);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(desk.getId());
    }

    @Test
    void whenConvertDeskToDeskDTO_withNullDesk_thenReturnNull() {
        DeskDTO result = deskMapper.deskToDeskDTO(null);

        assertThat(result).isNull();
    }

    @Test
    void whenConvertDeskDTOToDesk_thenReturnDesk() {
        DeskDTO deskDTO = DeskDTO.builder()
                .id(1L)
                .attendant(null)
                .openTicketsCount(3)
                .isDeleted(false)
                .build();

        Desk result = deskMapper.deskDTOToDesk(deskDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(deskDTO.getId());
    }
}