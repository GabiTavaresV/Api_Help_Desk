package com.api.helpdesk.mapper;

import com.api.helpdesk.dto.DeviceDTO;
import com.api.helpdesk.entity.Device;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class DeviceMapperTest {

    private final DeviceMapper deviceMapper = Mappers.getMapper(DeviceMapper.class);

    @Test
    void testToDto() {
        Device device = new Device();
        device.setId(1L);
        device.setSerialNumber("ABC123");

        DeviceDTO result = deviceMapper.toDto(device);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(device.getId());
        assertThat(result.getSerialNumber()).isEqualTo(device.getSerialNumber());
        assertThat(result.getIsDeleted()).isFalse();
    }

    @Test
    void testToDto_Null() {
        DeviceDTO result = deviceMapper.toDto(null);

        assertThat(result).isNull();
    }

    @Test
    void testToEntity() {
        DeviceDTO deviceDTO = DeviceDTO.builder()
                .id(1L)
                .serialNumber("ABC123")
                .isDeleted(false)
                .build();

        Device result = deviceMapper.toEntity(deviceDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(deviceDTO.getId());
        assertThat(result.getSerialNumber()).isEqualTo(deviceDTO.getSerialNumber());
    }
}
