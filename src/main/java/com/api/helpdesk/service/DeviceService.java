package com.api.helpdesk.service;

import com.api.helpdesk.entity.Device;
import com.api.helpdesk.exception.NotFoundDBException;
import com.api.helpdesk.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;


    public Device createDevice(Device device) {
        return deviceRepository.save(device);
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Device getDeviceById(Long id) throws NotFoundDBException {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundDBException("Aparelho não encontrado!"));

    }
}
