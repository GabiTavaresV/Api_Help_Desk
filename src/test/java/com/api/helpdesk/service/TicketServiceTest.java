package com.api.helpdesk.service;

import com.api.helpdesk.dto.*;
import com.api.helpdesk.entity.Ticket;
import com.api.helpdesk.exception.ConflictException;
import com.api.helpdesk.exception.ForbiddenException;
import com.api.helpdesk.exception.NotFoundDBException;
import com.api.helpdesk.mapper.TicketMapper;
import com.api.helpdesk.repository.TicketRepository;
import com.api.helpdesk.repository.WaitingLineRepository;
import com.api.helpdesk.utils.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private WaitingLineRepository waitingLineRepository;

    @Mock
    private DeskService deskService;

    @Mock
    private UserService userService;

    @Mock
    private DeviceService deviceService;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketService ticketService;

    private TicketRequest ticketRequest;
    private UserDTO userDTO;
    private DeviceDTO deviceDTO;
    private DeskDTO deskDTO;
    private TicketDTO ticketDTO;

    @BeforeEach
    void setUp() {
        ticketRequest = new TicketRequest();
        ticketRequest.setCustomerId(1L);
        ticketRequest.setDeviceId(1L);
        ticketRequest.setReason("Não liga");

        userDTO = new UserDTO();
        userDTO.setId(1L);

        deviceDTO = new DeviceDTO();
        deviceDTO.setId(1L);
        deviceDTO.setSerialNumber("ABC123");

        deskDTO = new DeskDTO();
        deskDTO.setId(1L);

        ticketDTO = new TicketDTO();
        ticketDTO.setId(1L);
        ticketDTO.setCustomer(userDTO);
        ticketDTO.setDevice(deviceDTO);
        ticketDTO.setDesk(deskDTO);
        ticketDTO.setReason(ticketRequest.getReason());
        ticketDTO.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateTicket_Success() throws NotFoundDBException {
        when(userService.getUserById(ticketRequest.getCustomerId())).thenReturn(userDTO);
        when(deviceService.getDeviceById(ticketRequest.getDeviceId())).thenReturn(deviceDTO);
        when(ticketRepository.countActiveTicketsByCustomerAndSerialNumber(ticketRequest.getCustomerId(), deviceDTO.getSerialNumber(), TicketStatus.ABERTO)).thenReturn(0L);
        when(ticketRepository.countActiveTicketsBySerialNumberNotConcluded(deviceDTO.getSerialNumber(), TicketStatus.CONCLUIDO)).thenReturn(0L);
        when(deskService.findAvailableDesks()).thenReturn(List.of(deskDTO));
        when(ticketMapper.toEntity(any())).thenReturn(new Ticket());
        when(ticketRepository.save(any(Ticket.class))).thenReturn(new Ticket());
        when(ticketMapper.toDTO(any())).thenReturn(ticketDTO);

        TicketDTO result = ticketService.createTicket(ticketRequest);

        assertNotNull(result);
        assertEquals(ticketDTO.getId(), result.getId());
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void testCreateTicket_NoAvailableDesk() throws NotFoundDBException {
        when(userService.getUserById(ticketRequest.getCustomerId())).thenReturn(userDTO);
        when(deviceService.getDeviceById(ticketRequest.getDeviceId())).thenReturn(deviceDTO);
        when(ticketRepository.countActiveTicketsByCustomerAndSerialNumber(ticketRequest.getCustomerId(), deviceDTO.getSerialNumber(), TicketStatus.ABERTO)).thenReturn(0L);
        when(ticketRepository.countActiveTicketsBySerialNumberNotConcluded(deviceDTO.getSerialNumber(), TicketStatus.CONCLUIDO)).thenReturn(0L);
        when(deskService.findAvailableDesks()).thenReturn(List.of());

        TicketDTO result = ticketService.createTicket(ticketRequest);

        assertNull(result);
        verify(waitingLineRepository).save(any());
    }

    @Test
    void testCreateTicket_Conflict() {
        when(userService.getUserById(ticketRequest.getCustomerId())).thenReturn(userDTO);
        when(deviceService.getDeviceById(ticketRequest.getDeviceId())).thenReturn(deviceDTO);
        when(ticketRepository.countActiveTicketsByCustomerAndSerialNumber(ticketRequest.getCustomerId(), deviceDTO.getSerialNumber(), TicketStatus.ABERTO)).thenReturn(1L);

        ConflictException thrown = assertThrows(ConflictException.class, () -> {
            ticketService.createTicket(ticketRequest);
        });
        assertEquals("O usuário já possui um chamado aberto para o mesmo serial number: ABC123", thrown.getMessage());
    }

    @Test
    void testCreateTicket_Forbidden() {
        when(userService.getUserById(ticketRequest.getCustomerId())).thenReturn(userDTO);
        when(deviceService.getDeviceById(ticketRequest.getDeviceId())).thenReturn(deviceDTO);
        when(ticketRepository.countActiveTicketsByCustomerAndSerialNumber(ticketRequest.getCustomerId(), deviceDTO.getSerialNumber(), TicketStatus.ABERTO)).thenReturn(0L);
        when(ticketRepository.countActiveTicketsBySerialNumberNotConcluded(deviceDTO.getSerialNumber(), TicketStatus.CONCLUIDO)).thenReturn(1L);

        ForbiddenException thrown = assertThrows(ForbiddenException.class, () -> {
            ticketService.createTicket(ticketRequest);
        });
        assertEquals("Outro chamado já está em atendimento para o serial number: ABC123", thrown.getMessage());
    }

    @Test
    void testCreateTicket_InvalidRequest() {
        ticketRequest.setCustomerId(null);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            ticketService.createTicket(ticketRequest);
        });
        assertEquals("Id do Cliente, Id do Aparelho e motivo do chamado são obrigatórios!.", thrown.getMessage());
    }

    @Test
    void testListAllTickets() {
        Pageable pageable = Pageable.ofSize(10);
        List<Ticket> tickets = List.of(new Ticket());
        Page<Ticket> ticketPage = new PageImpl<>(tickets, pageable, tickets.size());

        when(ticketRepository.findAll(pageable)).thenReturn(ticketPage);

        Page<Ticket> result = ticketService.listAllTickets(pageable);

        assertNotNull(result);
        assertEquals(tickets.size(), result.getContent().size());
        verify(ticketRepository).findAll(pageable);
    }

    @Test
    void testListTicketsByCustomerId() {
        Long customerId = 1L;
        Pageable pageable = Pageable.ofSize(10);
        List<Ticket> tickets = List.of(new Ticket());
        Page<Ticket> ticketPage = new PageImpl<>(tickets, pageable, tickets.size());

        when(ticketRepository.findByCustomerId(customerId, pageable)).thenReturn(ticketPage);

        Page<Ticket> result = ticketService.listTicketsByCustomerId(customerId, pageable);

        assertNotNull(result);
        assertEquals(tickets.size(), result.getContent().size());
        verify(ticketRepository).findByCustomerId(customerId, pageable);
    }

    @Test
    void testGetTicketDetails() throws NotFoundDBException {
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        Ticket result = ticketService.getTicketDetails(ticketId);

        assertNotNull(result);
        assertEquals(ticketId, result.getId());
        verify(ticketRepository).findById(ticketId);
    }

    @Test
    void testGetTicketDetails_NotFound() {
        Long ticketId = 1L;

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(NotFoundDBException.class, () -> {
            ticketService.getTicketDetails(ticketId);
        });
    }

    @Test
    void testListTicketsByDeskId() {
        Long deskId = 1L;
        Pageable pageable = Pageable.ofSize(10);
        List<Ticket> tickets = List.of(new Ticket());
        Page<Ticket> ticketPage = new PageImpl<>(tickets, pageable, tickets.size());

        when(ticketRepository.findByDeskId(deskId, pageable)).thenReturn(ticketPage);

        Page<Ticket> result = ticketService.listTicketsByDeskId(deskId, pageable);

        assertNotNull(result);
        assertEquals(tickets.size(), result.getContent().size());
        verify(ticketRepository).findByDeskId(deskId, pageable);
    }

    @Test
    void testDeleteTicketById() throws NotFoundDBException {
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        ticketService.deleteTicketById(ticketId);

        verify(ticketRepository).softDeleteTicketById(ticketId);
    }

    @Test
    void testDeleteTicketById_NotFound() {
        Long ticketId = 1L;

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(NotFoundDBException.class, () -> ticketService.deleteTicketById(ticketId));
    }

    @Test
    void testUpdateStatusById() throws NotFoundDBException {
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setStatus(TicketStatus.ABERTO);

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        ticketService.updateStatusById(ticketId, TicketStatus.CONCLUIDO);

        assertEquals(TicketStatus.CONCLUIDO, ticket.getStatus());
        assertNotNull(ticket.getResolvedDate());
        verify(ticketRepository).save(ticket);
    }

    @Test
    void testUpdateStatusById_NotFound() {
        Long ticketId = 1L;

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(NotFoundDBException.class, () -> ticketService.updateStatusById(ticketId, TicketStatus.CONCLUIDO));
    }
}
