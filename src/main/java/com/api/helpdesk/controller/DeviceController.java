package com.api.helpdesk.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.helpdesk.dto.DeviceDTO;
import com.api.helpdesk.service.DeviceService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/device")
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    public ResponseEntity<DeviceDTO> create(@Valid @RequestBody DeviceDTO device) {
        DeviceDTO createDevice = deviceService.createDevice(device);
        return new ResponseEntity<>(createDevice, HttpStatus.CREATED);
    }

    @GetMapping("/findAll")
    public ResponseEntity<Page<DeviceDTO>> getAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<DeviceDTO> list = deviceService.getAllDevices(pageable);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDTO> getById(@PathVariable Long id) {
        DeviceDTO device = deviceService.getDeviceById(id);
        return new ResponseEntity<>(device, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deviceService.deleteDeviceById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
