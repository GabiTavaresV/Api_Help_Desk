package com.api.helpdesk.service;

import com.api.helpdesk.exception.AttendantAlreadyExistsException;
import com.api.helpdesk.dto.AttendantDTO;
import com.api.helpdesk.entity.Attendant;
import com.api.helpdesk.exception.NotFoundDBException;
import com.api.helpdesk.exception.SoftDeleteException;
import com.api.helpdesk.mapper.AttendantMapper;
import com.api.helpdesk.repository.AttendantRepository;
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
class AttendantServiceTest {

    @Mock
    private AttendantRepository attendantRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private AttendantMapper attendantMapper;

    @InjectMocks
    private AttendantService attendantService;

    private AttendantDTO attendantDTO;
    private Attendant attendant;

    @BeforeEach
    void setUp() {
        attendantDTO = new AttendantDTO();
        attendant = new Attendant();
    }

    @Test
    void testRegister() {
        attendantDTO.setName("Attendant Test");
        when(attendantRepository.existsByName(attendantDTO.getName())).thenReturn(false);
        when(attendantMapper.attendantDTOToAttendant(attendantDTO)).thenReturn(attendant);
        when(attendantRepository.save(attendant)).thenReturn(attendant);
        when(attendantMapper.attendantToAttendantDTO(attendant)).thenReturn(attendantDTO);

        AttendantDTO result = attendantService.register(attendantDTO);

        verify(attendantRepository).save(attendant);
        assertThat(result).isEqualTo(attendantDTO);
    }

    @Test
    void testRegister_AttendantAlreadyExists() {
        attendantDTO.setName("Attendant Test");
        when(attendantRepository.existsByName(attendantDTO.getName())).thenReturn(true);

        assertThrows(AttendantAlreadyExistsException.class, () -> attendantService.register(attendantDTO)); // Alterado para a exceção correta
    }

    @Test
    void testGetAllAttendants() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Attendant> attendantsPage = new PageImpl<>(List.of(attendant));
        when(attendantRepository.findAllActiveAttendants(pageable)).thenReturn(attendantsPage);
        when(attendantMapper.attendantToAttendantDTO(attendant)).thenReturn(attendantDTO);

        Page<AttendantDTO> result = attendantService.getAllAttendants(pageable);

        assertThat(result.getContent()).containsExactly(attendantDTO);
    }

    @Test
    void testGetAttendantById() throws NotFoundDBException {
        Long id = 1L;
        when(attendantRepository.findActiveAttendantById(id)).thenReturn(Optional.of(attendant));
        when(attendantMapper.attendantToAttendantDTO(attendant)).thenReturn(attendantDTO);

        AttendantDTO result = attendantService.getAttendantById(id);

        assertThat(result).isEqualTo(attendantDTO);
    }

    @Test
    void testGetAttendantById_NotFound() {
        Long id = 1L;
        when(attendantRepository.findActiveAttendantById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundDBException.class, () -> attendantService.getAttendantById(id));
    }

    @Test
    void testDeleteAttendantById() throws NotFoundDBException {
        Long id = 1L;
        when(attendantRepository.findById(id)).thenReturn(Optional.of(attendant));
        attendantService.deleteAttendantById(id);

        verify(attendantRepository).softDeleteAttendantById(id);
    }

    @Test
    void testDeleteAttendantById_NotFound() {
        Long id = 1L;
        when(attendantRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundDBException.class, () -> attendantService.deleteAttendantById(id));
    }

    @Test
    void testDeleteAttendantById_WithOpenTickets() {
        Long id = 1L;
        when(attendantRepository.findById(id)).thenReturn(Optional.of(new Attendant()));
        when(ticketRepository.countTicketsByAttendantIdAndNotConcluded(id, TicketStatus.CONCLUIDO)).thenReturn(1L); // Existe 1 chamado aberto

        assertThrows(SoftDeleteException.class, () -> attendantService.deleteAttendantById(id));
    }

    @Test
    void testDeleteAttendantById_Success() throws NotFoundDBException {
        Long id = 1L;
        when(attendantRepository.findById(id)).thenReturn(Optional.of(new Attendant()));
        when(ticketRepository.countTicketsByAttendantIdAndNotConcluded(id, TicketStatus.CONCLUIDO)).thenReturn(0L);

        attendantService.deleteAttendantById(id);

        verify(attendantRepository).softDeleteAttendantById(id);
    }
}

