package com.api.helpdesk.controllers;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.api.helpdesk.controller.TicketController;
import com.api.helpdesk.dto.*;
import com.api.helpdesk.entity.Desk;
import com.api.helpdesk.entity.Device;
import com.api.helpdesk.entity.Ticket;
import com.api.helpdesk.entity.Users;
import com.api.helpdesk.service.TicketService;
import com.api.helpdesk.utils.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

public class TicketControllerTest {

    private TicketController ticketController;
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketService = mock(TicketService.class);
        ticketController = new TicketController(ticketService);
    }

    @Test
    void deveCriarTicket() {
        TicketRequest ticketRequest = new TicketRequest();
        TicketDTO createdTicket = new TicketDTO();
        DeskDTO desk = new DeskDTO();
        DeviceDTO device = new DeviceDTO();
        UserDTO user = new UserDTO();
        createdTicket.setId(1L);
        createdTicket.setReason("Nao passa o cartao");
        createdTicket.setDesk(desk);
        createdTicket.setDevice(device);
        createdTicket.setCustomer(user);


        when(ticketService.createTicket(any(TicketRequest.class)))
                .thenReturn(createdTicket);

        ResponseEntity<TicketDTO> response = ticketController.create(ticketRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getReason()).isEqualTo("Nao passa o cartao");

        verify(ticketService).createTicket(any(TicketRequest.class));
    }

    @Test
    void deveRetornarListaDeTickets() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Ticket ticket1 = new Ticket();
        ticket1.setId(1L);
        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);

        Page<Ticket> page = new PageImpl<>(Arrays.asList(ticket1, ticket2), pageable, 2);

        when(ticketService.listAllTickets(pageable)).thenReturn(page);

        ResponseEntity<Page<Ticket>> response = ticketController.getAll(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(2);
        assertThat(response.getBody().getContent()).hasSize(2);
        assertThat(response.getBody().getContent().get(0).getId()).isEqualTo(1L);
        assertThat(response.getBody().getContent().get(1).getId()).isEqualTo(2L);

        verify(ticketService).listAllTickets(pageable);
    }

    @Test
    void deveRetornarTicketPorId() {
        Users user = new Users();
        Device device = new Device();
        Desk desk = new Desk();
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setCustomer(user);
        ticket.setDevice(device);
        ticket.setDesk(desk);
        ticket.setReason("Nao liga");

        when(ticketService.getTicketDetails(ticketId)).thenReturn(ticket);

        ResponseEntity<Ticket> response = ticketController.getById(ticketId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(ticketId);
        assertThat(response.getBody().getReason()).isEqualTo("Nao liga");

        verify(ticketService).getTicketDetails(ticketId);
    }

    @Test
    void deveRetornarTicketsPorDeskId() {
        Long deskId = 1L;
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Ticket ticket1 = new Ticket();
        ticket1.setId(1L);
        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);

        Page<Ticket> page = new PageImpl<>(Arrays.asList(ticket1, ticket2), pageable, 2);

        when(ticketService.listTicketsByDeskId(deskId, pageable)).thenReturn(page);

        ResponseEntity<Page<Ticket>> response = ticketController.getTicketsByDesk(deskId, pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(2);
        assertThat(response.getBody().getContent()).hasSize(2);

        verify(ticketService).listTicketsByDeskId(deskId, pageable);
    }

    @Test
    void deveRetornarTicketsPorCustomerId() {
        Long customerId = 1L;
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Ticket ticket1 = new Ticket();
        ticket1.setId(1L);
        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);

        Page<Ticket> page = new PageImpl<>(Arrays.asList(ticket1, ticket2), pageable, 2);

        when(ticketService.listTicketsByCustomerId(customerId, pageable)).thenReturn(page);

        ResponseEntity<Page<Ticket>> response = ticketController.getTicketsByCustomerId(customerId, pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(2);
        assertThat(response.getBody().getContent()).hasSize(2);

        verify(ticketService).listTicketsByCustomerId(customerId, pageable);
    }

    @Test
    void deveDeletarTicket() {
        Long ticketId = 1L;

        ResponseEntity<Void> response = ticketController.delete(ticketId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(ticketService).deleteTicketById(ticketId);
    }

    @Test
    void deveAtualizarStatusDoTicket() {
        Long ticketId = 1L;
        TicketStatusUpdateDTO statusUpdate = new TicketStatusUpdateDTO();
        statusUpdate.setStatus(TicketStatus.CONCLUIDO);

        ResponseEntity<Void> response = ticketController.updateStatus(ticketId, statusUpdate);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(ticketService).updateStatusById(ticketId, statusUpdate.getStatus());
    }
}