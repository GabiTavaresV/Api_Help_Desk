package com.api.helpdesk.controller;


import com.api.helpdesk.dto.DeviceDTO;
import com.api.helpdesk.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping
    public DeviceDTO create(@RequestBody DeviceDTO device) {
        return deviceService.createDevice(device);
    }

    @GetMapping("/findAll")
    public List<DeviceDTO> getAll() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/{id}")
    public DeviceDTO getById(@PathVariable Long id) {
        return deviceService.getDeviceById(id);
    }
}
