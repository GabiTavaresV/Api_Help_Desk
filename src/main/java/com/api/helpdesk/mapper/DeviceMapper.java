package com.api.helpdesk.mapper;

import org.springframework.stereotype.Component;

import com.api.helpdesk.dto.DeviceDTO;
import com.api.helpdesk.entity.Device;

@Component
public class DeviceMapper {

    public DeviceDTO toDTO(Device device) {
        if (device == null) {
            return null;
        }
       DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setId(device.getId());
        deviceDTO.setSerialNumber(device.getSerialNumber());
        deviceDTO.setIsDeleted(device.getIsDeleted());

        return deviceDTO;
    }

    public Device toEntity(DeviceDTO deviceDTO) {
        if (deviceDTO == null) {
            return null;
        }
        Device device = new Device();
        device.setId(deviceDTO.getId());
        device.setSerialNumber(deviceDTO.getSerialNumber());
        device.setIsDeleted(deviceDTO.getIsDeleted() != null ? deviceDTO.getIsDeleted() : false);
        return device;
    }
}
