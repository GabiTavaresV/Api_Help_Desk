package com.api.helpdesk.mapper;

import com.api.helpdesk.dto.DeskDTO;
import com.api.helpdesk.entity.Desk;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface DeskMapper {

DeskDTO deskToDeskDTO(Desk desk);

Desk deskDTOToDesk(DeskDTO deskDTO);

}