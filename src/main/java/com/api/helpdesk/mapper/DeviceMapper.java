package com.api.helpdesk.mapper;

import com.api.helpdesk.dto.DeviceDTO;
import com.api.helpdesk.entity.Device;

public class DeviceMapper {

    public DeviceDTO toDTO(Device device) {
        if (device == null) {
            return null;
        }
        return new DeviceDTO(device.getId(), device.getSerialNumber());
    }

    public Device toEntity(DeviceDTO deviceDTO) {
        if (deviceDTO == null) {
            return null;
        }
        Device device = new Device();
        device.setId(deviceDTO.getId());
        device.setSerialNumber(deviceDTO.getSerialNumber());
        return device;
    }
}
