package com.api.helpdesk.mapper;

import org.springframework.stereotype.Component;

import com.api.helpdesk.dto.AttendantDTO;
import com.api.helpdesk.entity.Attendant;


@Component
public class AttendantMapper {

    public AttendantDTO toDTO(Attendant attendant) {
        if (attendant == null) {
            return null;
        }
        AttendantDTO attendantDTO = new AttendantDTO();
        attendantDTO.setId(attendant.getId());
        attendantDTO.setName(attendant.getName());
        attendantDTO.setIsDeleted(attendant.getIsDeleted());
        return attendantDTO;
    }

    public Attendant toEntity(AttendantDTO attendantDTO) {
        if (attendantDTO == null) {
            return null;
        }
        Attendant attendant = new Attendant();
        attendant.setId(attendantDTO.getId());
        attendant.setName(attendantDTO.getName());
        attendant.setIsDeleted(attendantDTO.getIsDeleted() != null ? attendantDTO.getIsDeleted() : false);
        return attendant;
    }
}
