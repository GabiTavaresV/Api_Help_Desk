package com.api.helpdesk.repository;


import com.api.helpdesk.entity.Device;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class DeviceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DeviceRepository deviceRepository;

    private Device device;

    @BeforeEach
    public void setUp() {
        device = new Device();
        device.setSerialNumber("SN123456");
        device.setIsDeleted(false);
        entityManager.persistAndFlush(device);
    }

    @Test
    public void whenFindAllActiveDevices_thenReturnActiveDevices() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Device> activeDevices = deviceRepository.findAllActiveDevices(pageable);
       Assertions.assertThat(activeDevices.hasContent()).isTrue();
        assertTrue(activeDevices.getContent().contains(device));
        Assertions.assertThat(activeDevices.getTotalElements()).isGreaterThan(0);
        Assertions.assertThat(activeDevices.getSize()).isLessThanOrEqualTo(10);
    }

    @Test
    public void whenFindActiveDeviceById_thenReturnActiveDevice() {
        Optional<Device> foundDevice = deviceRepository.findActiveDeviceById(device.getId());
        assertTrue(foundDevice.isPresent());
        assertThat(foundDevice.get().getSerialNumber()).isEqualTo(device.getSerialNumber());
    }

    @Test
    public void whenSoftDeleteByDeviceId_thenDeviceIsDeleted() {
        deviceRepository.softDeleteByDeviceId(device.getId());
        Optional<Device> deletedDevice = deviceRepository.findActiveDeviceById(device.getId());
        assertTrue(deletedDevice.isEmpty());
    }

    @Test
    public void whenCheckForExistingDeviceBySerialNumber_thenReturnTrue() {
        boolean exists = deviceRepository.existsBySerialNumber(device.getSerialNumber());
        assertTrue(exists);
    }

    @Test
    public void whenCheckForNonExistingDeviceBySerialNumber_thenReturnFalse() {
        boolean exists = deviceRepository.existsBySerialNumber("SN999999");
        assertThat(exists).isFalse();
    }


}
