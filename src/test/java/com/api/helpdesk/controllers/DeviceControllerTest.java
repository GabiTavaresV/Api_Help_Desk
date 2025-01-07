package com.api.helpdesk.controllers;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.api.helpdesk.controller.DeviceController;
import com.api.helpdesk.dto.DeviceDTO;
import com.api.helpdesk.service.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

public class DeviceControllerTest {

    private DeviceController deviceController;
    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        deviceService = mock(DeviceService.class);
        deviceController = new DeviceController(deviceService);
    }

    @Test
    void whenPostDevice_thenCreateDevice() {
        DeviceDTO device = new DeviceDTO();
        device.setSerialNumber("BRT0988TESTE");

        when(deviceService.createDevice(any(DeviceDTO.class)))
                .thenReturn(device);

        ResponseEntity<DeviceDTO> response = deviceController.create(device);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSerialNumber()).isEqualTo("BRT0988TESTE");

        verify(deviceService).createDevice(any(DeviceDTO.class));
    }

    @Test
    void whenGetDevices_thenListAllDevices() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        DeviceDTO device1 = new DeviceDTO();
        device1.setSerialNumber("BRT0977TESTE");
        DeviceDTO device2 = new DeviceDTO();
        device2.setSerialNumber("BRT0867TESTE");

        Page<DeviceDTO> page = new PageImpl<>(Arrays.asList(device1, device2), pageable, 2);

        when(deviceService.getAllDevices(pageable)).thenReturn(page);

        ResponseEntity<Page<DeviceDTO>> response = deviceController.getAll(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(2);
        assertThat(response.getBody().getContent()).hasSize(2);
        assertThat(response.getBody().getContent().get(0).getSerialNumber()).isEqualTo("BRT0977TESTE");
        assertThat(response.getBody().getContent().get(1).getSerialNumber()).isEqualTo("BRT0867TESTE");

        verify(deviceService).getAllDevices(pageable);
    }

    @Test
    void whenGetDeviceById_thenReturnDevice() {
        Long deviceId = 1L;
        DeviceDTO device = new DeviceDTO();
        device.setSerialNumber("BRC0860TESTE");

        when(deviceService.getDeviceById(deviceId)).thenReturn(device);

        ResponseEntity<DeviceDTO> response = deviceController.getById(deviceId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSerialNumber()).isEqualTo("BRC0860TESTE");

        verify(deviceService).getDeviceById(deviceId);
    }

    @Test
    void whenDeleteDeviceById_thenDeviceIsDeleted() {
        Long deviceId = 1L;

        ResponseEntity<Void> response = deviceController.delete(deviceId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(deviceService).deleteDeviceById(deviceId);
    }
}