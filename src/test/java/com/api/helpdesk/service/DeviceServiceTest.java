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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private DeviceMapper deviceMapper;

    @InjectMocks
    private DeviceService deviceService;

    private DeviceDTO deviceDTO;
    private Device device;

    @BeforeEach
    void setUp() {
        deviceDTO = new DeviceDTO();
        device = new Device();
    }

    @Test
    void testRegister() {
        deviceDTO.setSerialNumber("BRTO-test");
        when(deviceRepository.existsBySerialNumber(deviceDTO.getSerialNumber())).thenReturn(false);
        when(deviceMapper.toEntity(deviceDTO)).thenReturn(device);
        when(deviceRepository.save(device)).thenReturn(device);
        when(deviceMapper.toDto(device)).thenReturn(deviceDTO);

        DeviceDTO result = deviceService.createDevice(deviceDTO);

        verify(deviceRepository).save(device);
        assertThat(result).isEqualTo(deviceDTO);
    }

    @Test
    void testeRegisterUser_SerialNumberExistes() {
        deviceDTO.setSerialNumber("BRTO-test");
        when(deviceRepository.existsBySerialNumber(deviceDTO.getSerialNumber())).thenReturn(true);

        assertThrows(DeviceAlreadyExistsException.class, () -> deviceService.createDevice(deviceDTO)); // Mudança aqui
    }

    @Test
    void testGetAllDevices() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Device> devicePage = new PageImpl<>(List.of(device));
        when(deviceRepository.findAllActiveDevices(pageable)).thenReturn(devicePage);
        when(deviceMapper.toDto(device)).thenReturn(deviceDTO);

        Page<DeviceDTO> result = deviceService.getAllDevices(pageable);

        assertThat(result.getContent()).containsExactly(deviceDTO);
    }

    @Test
    void testGetAttendantById() throws NotFoundDBException {
        Long id = 1L;
        when(deviceRepository.findActiveDeviceById(id)).thenReturn(Optional.of(device));
        when(deviceMapper.toDto(device)).thenReturn(deviceDTO);

        DeviceDTO result = deviceService.getDeviceById(id);

        assertThat(result).isEqualTo(deviceDTO);
    }

    @Test
    void testGetAttendantById_NotFound() {
        Long id = 1L;
        when(deviceRepository.findActiveDeviceById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundDBException.class, () -> deviceService.getDeviceById(id));
    }

    @Test
    void testDeleteDeviceById() throws NotFoundDBException {
        Long id = 1L;
        when(deviceRepository.findById(id)).thenReturn(Optional.of(device));

        // Mock para o método do ticketRepository
        when(ticketRepository.countTicketsByDeviceIdAndNotConcluded(id, TicketStatus.CONCLUIDO))
                .thenReturn(0L);

        deviceService.deleteDeviceById(id);

        verify(deviceRepository).softDeleteByDeviceId(id);
    }

    @Test
    void testDeleteAttendantById_NotFound() {
        Long id = 1L;
        when(deviceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundDBException.class, () -> deviceService.deleteDeviceById(id));
    }

    @Test
    void testDeleteDeviceById_HasOpenTickets() {
        Long id = 1L;
        when(deviceRepository.findById(id)).thenReturn(Optional.of(device));

        when(ticketRepository.countTicketsByDeviceIdAndNotConcluded(id, TicketStatus.CONCLUIDO))
                .thenReturn(1L);

        assertThrows(SoftDeleteException.class, () -> deviceService.deleteDeviceById(id));
    }
}
