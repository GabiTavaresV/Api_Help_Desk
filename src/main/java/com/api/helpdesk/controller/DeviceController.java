package com.api.helpdesk.controller;


import com.api.helpdesk.dto.DeviceDTO;
import com.api.helpdesk.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping
    public ResponseEntity<DeviceDTO> create(@RequestBody DeviceDTO device) {
        DeviceDTO createDevice = deviceService.createDevice(device);
        return new ResponseEntity<>(createDevice, HttpStatus.CREATED);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<DeviceDTO>> getAll() {
        List<DeviceDTO> list = deviceService.getAllDevices();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDTO> getById(@PathVariable Long id) {
        DeviceDTO device = deviceService.getDeviceById(id);
        return new ResponseEntity<>(device, HttpStatus.OK);
    }
}
