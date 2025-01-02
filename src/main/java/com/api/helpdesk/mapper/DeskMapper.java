package com.api.helpdesk.mapper;

import org.springframework.stereotype.Component;

import com.api.helpdesk.dto.DeskDTO;
import com.api.helpdesk.entity.Desk;

@Component
public class DeskMapper {

    private final AttendantMapper attendantMapper = new AttendantMapper();

    public DeskDTO toDTO(Desk desk) {
        if (desk == null) {
            return null;
        }
        DeskDTO deskDTO = new DeskDTO();
        deskDTO.setId(desk.getId());
        deskDTO.setAttendant(attendantMapper.toDTO(desk.getAttendant()));
        deskDTO.setIsDeleted(desk.getIsDeleted());
        return deskDTO;
    }

    public Desk toEntity(DeskDTO deskDTO) {
        if (deskDTO == null) {
            return null;
        }
        Desk desk = new Desk();
        desk.setId(deskDTO.getId());
        desk.setAttendant(attendantMapper.toEntity(deskDTO.getAttendant()));
        desk.setIsDeleted(deskDTO.getIsDeleted() != null ? deskDTO.getIsDeleted() : false);
        return desk;
    }
}