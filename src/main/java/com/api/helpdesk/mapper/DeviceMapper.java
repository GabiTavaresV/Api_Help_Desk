package com.api.helpdesk.mapper;

import org.mapstruct.Mapper;

import com.api.helpdesk.dto.DeviceDTO;
import com.api.helpdesk.entity.Device;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    default DeviceDTO toDto (Device device) {
        if (device == null) {
            return null;
        }

        return DeviceDTO.builder()
                .id(device.getId())
                .serialNumber(device.getSerialNumber())
                .isDeleted(false)
                .build();
    }


    Device toEntity(DeviceDTO deviceDTO);


}
