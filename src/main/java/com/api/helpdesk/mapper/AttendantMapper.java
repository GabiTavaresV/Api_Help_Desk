package com.api.helpdesk.mapper;

import com.api.helpdesk.dto.AttendantDTO;
import com.api.helpdesk.entity.Attendant;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface AttendantMapper {

    default AttendantDTO attendantToAttendantDTO(Attendant attendant) {
        if (attendant == null) {
            return null;
        }

        return AttendantDTO.builder()
                .id(attendant.getId())
                .name(attendant.getName())
                .isDeleted(false)
                .build();
    }

    Attendant attendantDTOToAttendant(AttendantDTO attendantDTO);

}
