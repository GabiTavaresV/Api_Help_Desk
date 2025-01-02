package com.api.helpdesk.mapper;

import com.api.helpdesk.dto.DeskDTO;
import com.api.helpdesk.entity.Desk;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeskMapper {

    DeskDTO deskToDeskDTO(Desk desk);
    List<DeskDTO> desksToDeskDTOs(List<Desk> desks);

    Desk deskDTOToDesk(DeskDTO deskDTO);
    List<Desk> deskDTOsToDesks(List<DeskDTO> deskDTOs);

}