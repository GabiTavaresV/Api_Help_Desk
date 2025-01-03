package com.api.helpdesk.service;

import com.api.helpdesk.dto.DeviceDTO;
import com.api.helpdesk.entity.Device;
import com.api.helpdesk.exception.DeviceAlreadyExistsException;
import com.api.helpdesk.exception.NotFoundDBException;
import com.api.helpdesk.exception.SoftDeleteException;
import com.api.helpdesk.mapper.DeviceMapper;
import com.api.helpdesk.repository.DeviceRepository;
import com.api.helpdesk.repository.TicketRepository;
import com.api.helpdesk.utils.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private  DeviceMapper deviceMapper;

    public DeviceDTO createDevice(DeviceDTO deviceDTO) {
        String serialNumber = deviceDTO.getSerialNumber();
        if (deviceRepository.existsBySerialNumber(serialNumber)) {
            throw new DeviceAlreadyExistsException("Dispositivo já cadastrado.");
        }
        Device device = deviceMapper.toEntity(deviceDTO);
        Device savedDevice = deviceRepository.save(device);
        return deviceMapper.toDTO(savedDevice);
    }

    public Page<DeviceDTO> getAllDevices(Pageable pageable) {
        Page<Device> devices = deviceRepository.findAllActiveDevices(pageable);
        return devices.map(deviceMapper::toDTO);
    }

    public DeviceDTO getDeviceById(Long id) throws NotFoundDBException {
        Device device = deviceRepository.findActiveDeviceById(id)
                .orElseThrow(() -> new NotFoundDBException("Aparelho não encontrado!"));
        return deviceMapper.toDTO(device);
    }

    public Void deleteDeviceById(Long id) throws NotFoundDBException {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (deviceOptional.isEmpty()) {
            throw new NotFoundDBException("Equipamento não encontrado!");
        }

        long activeTicketsCount = ticketRepository.countTicketsByDeviceIdAndNotConcluded(id, TicketStatus.CONCLUIDO);

        if (activeTicketsCount > 0) {
            throw new SoftDeleteException("Não é possível deletar o dispositivo. Existem chamados não concluídos vinculados.");
        }
        deviceRepository.softDeleteByDeviceId(id);
        return null;
    }

}
