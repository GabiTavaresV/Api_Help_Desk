package com.api.helpdesk.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.api.helpdesk.entity.Ticket;
import com.api.helpdesk.repository.TicketRepository;
import com.api.helpdesk.utils.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JobTicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private JobTicket jobTicket;

    private List<Ticket> tickets;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tickets = new ArrayList<>();
    }

    @Test
    void testProcessTickets_OpenToWaiting() {
        Ticket ticket1 = new Ticket();
        ticket1.setStatus(TicketStatus.ABERTO);
        LocalDateTime originalUpdatedAt = LocalDateTime.now().minusSeconds(31);
        ticket1.setUpdatedAt(originalUpdatedAt);

        tickets.add(ticket1);

        when(ticketRepository.findAll()).thenReturn(tickets);

        jobTicket.processTickets();

        assertThat(ticket1.getStatus()).isEqualTo(TicketStatus.EM_ESPERA);

        assertThat(ticket1.getUpdatedAt()).isAfter(originalUpdatedAt);

        verify(ticketRepository).save(ticket1);
    }

    @Test
    void testProcessTickets_WaitingToInProgress() {
        Ticket ticket2 = new Ticket();
        ticket2.setStatus(TicketStatus.EM_ESPERA);
        LocalDateTime originalUpdatedAt = LocalDateTime.now().minusSeconds(31);
        ticket2.setUpdatedAt(originalUpdatedAt);

        tickets.add(ticket2);
        when(ticketRepository.findAll()).thenReturn(tickets);

        jobTicket.processTickets();

        assertThat(ticket2.getStatus()).isEqualTo(TicketStatus.EM_ATENDIMENTO);

        assertThat(ticket2.getUpdatedAt()).isAfter(originalUpdatedAt);
        verify(ticketRepository).save(ticket2);
    }

    @Test
    void testProcessTickets_InProgressToCompleted() {
        Ticket ticket3 = new Ticket();
        ticket3.setStatus(TicketStatus.EM_ATENDIMENTO);
        LocalDateTime originalUpdatedAt = LocalDateTime.now().minusSeconds(31);
        ticket3.setUpdatedAt(originalUpdatedAt);

        tickets.add(ticket3);
        when(ticketRepository.findAll()).thenReturn(tickets);

        jobTicket.processTickets();

        assertThat(ticket3.getStatus()).isEqualTo(TicketStatus.CONCLUIDO);

        assertThat(ticket3.getUpdatedAt()).isAfter(originalUpdatedAt);

        assertThat(ticket3.getResolvedDate()).isAfter(originalUpdatedAt);

        verify(ticketRepository).save(ticket3);
    }

    @Test
    void testProcessTickets_NoUpdateNeeded() {
        Ticket ticket4 = new Ticket();
        ticket4.setStatus(TicketStatus.ABERTO);
        ticket4.setUpdatedAt(LocalDateTime.now().minusSeconds(10));

        tickets.add(ticket4);
        when(ticketRepository.findAll()).thenReturn(tickets);

        jobTicket.processTickets();

        assertThat(ticket4.getStatus()).isEqualTo(TicketStatus.ABERTO);
        verify(ticketRepository, never()).save(ticket4);
    }
}
