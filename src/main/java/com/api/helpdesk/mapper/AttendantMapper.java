package com.api.helpdesk.mapper;

import com.api.helpdesk.dto.AttendantDTO;
import com.api.helpdesk.entity.Attendant;

public class AttendantMapper {

    public AttendantDTO toDTO(Attendant attendant) {
        if (attendant == null) {
            return null;
        }
        AttendantDTO attendantDTO = new AttendantDTO();
        attendantDTO.setId(attendant.getId());
        attendantDTO.setName(attendant.getName());
        return attendantDTO;
    }



    public Attendant toEntity(AttendantDTO attendantDTO) {
        if (attendantDTO == null) {
            return null;
        }
        Attendant attendant = new Attendant();
        attendant.setId(attendantDTO.getId());
        attendant.setName(attendantDTO.getName());
        return attendant;
    }
}
