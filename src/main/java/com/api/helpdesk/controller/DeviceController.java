package com.api.helpdesk.controller;


import com.api.helpdesk.entity.Device;
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
    public Device create(@RequestBody Device device) {
        return deviceService.createDevice(device);
    }

    @GetMapping("/findAll")
    public List<Device> getAll() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/{id}")
    public Device getById(@PathVariable Long id) {
        return deviceService.getDeviceById(id);
    }
}
