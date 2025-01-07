package com.api.helpdesk.service;

import com.api.helpdesk.dto.DeskDTO;
import com.api.helpdesk.dto.TicketRequest;
import com.api.helpdesk.entity.WaitingLine;
import com.api.helpdesk.repository.WaitingLineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WaitingLineServiceTest {

    @Mock
    private WaitingLineRepository waitingTicketRepository;

    @Mock
    private TicketService ticketService;

    @Mock
    private DeskService deskService;

    @InjectMocks
    private WaitingLineService waitingLineService;

    private WaitingLine waitingTicket;

    @BeforeEach
    void setUp() {
        waitingTicket = new WaitingLine();
        waitingTicket.setCustomerId(1L);
        waitingTicket.setDeviceId(1L);
        waitingTicket.setReason("Test reason");
    }

    @Test
    void testProcessWaitingTickets_WithAvailableDesks() {
        // Setup
        List<WaitingLine> waitingTickets = new ArrayList<>();
        waitingTickets.add(waitingTicket);

        when(waitingTicketRepository.findAll()).thenReturn(waitingTickets);

        DeskDTO deskDTO = new DeskDTO();
        deskDTO.setId(1L);
        List<DeskDTO> availableDesks = List.of(deskDTO);
        when(deskService.findAvailableDesks()).thenReturn(availableDesks);

        waitingLineService.processWaitingTickets();

        TicketRequest ticketRequest = new TicketRequest();
        ticketRequest.setCustomerId(waitingTicket.getCustomerId());
        ticketRequest.setDeviceId(waitingTicket.getDeviceId());
        ticketRequest.setReason(waitingTicket.getReason());

        verify(ticketService).createTicket(ticketRequest);
        verify(waitingTicketRepository).delete(waitingTicket);
    }

    @Test
    void testProcessWaitingTickets_WithNoAvailableDesks() {
        List<WaitingLine> waitingTickets = new ArrayList<>();
        waitingTickets.add(waitingTicket);

        when(waitingTicketRepository.findAll()).thenReturn(waitingTickets);
        when(deskService.findAvailableDesks()).thenReturn(new ArrayList<>());

        waitingLineService.processWaitingTickets();

        verify(ticketService, never()).createTicket(any());
        verify(waitingTicketRepository, never()).delete(waitingTicket);
    }

    @Test
    void testProcessWaitingTickets_WithNoWaitingTickets() {
        when(waitingTicketRepository.findAll()).thenReturn(new ArrayList<>());

        waitingLineService.processWaitingTickets();

        verify(ticketService, never()).createTicket(any());
        verify(waitingTicketRepository, never()).delete(any());
    }
}