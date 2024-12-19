package com.api.helpdesk.service;

import com.api.helpdesk.dto.DeviceDTO;
import com.api.helpdesk.entity.Device;
import com.api.helpdesk.exception.ConflictException;
import com.api.helpdesk.exception.NotFoundDBException;
import com.api.helpdesk.mapper.DeviceMapper;
import com.api.helpdesk.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    private final DeviceMapper deviceMapper = new DeviceMapper();

    public DeviceDTO createDevice(DeviceDTO deviceDTO) {
        String serialNumber = deviceDTO.getSerialNumber();
        if (deviceRepository.existsBySerialNumber(serialNumber)) {
            throw new ConflictException("Conflito: Já existe um dispositivo cadastrado com o mesmo serial number: " + serialNumber);
        }
        Device device = deviceMapper.toEntity(deviceDTO);
        Device savedDevice = deviceRepository.save(device);
        return deviceMapper.toDTO(savedDevice);
    }

    public List<DeviceDTO> getAllDevices() {
        List<Device> devices = deviceRepository.findAllActiveDevices();
        return devices.stream()
                .map(deviceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO getDeviceById(Long id) throws NotFoundDBException {
        Device device = deviceRepository.findActiveDeviceById(id)
                .orElseThrow(() -> new NotFoundDBException("Aparelho não encontrado!"));
        return deviceMapper.toDTO(device);
    }

    public Void deleteDeviceById(Long id) throws NotFoundDBException {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            throw new NotFoundDBException("Equipamento não encontrado!");
        }
        deviceRepository.softDeleteByDeviceId(id);
        return null;
    }

}
