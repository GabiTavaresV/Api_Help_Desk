package com.api.helpdesk.service;


import com.api.helpdesk.entity.Ticket;
import com.api.helpdesk.repository.TicketRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.api.helpdesk.utils.TicketStatus;

import java.util.List;

@Service
public class JobTicket {

    private final TicketRepository ticketRepository;

    public JobTicket(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Scheduled(fixedRate = 120000)
    @Transactional
    public void processTickets() {
        List<Ticket> openTickets = ticketRepository.findAllByStatus(TicketStatus.ABERTO);
        for (Ticket ticket : openTickets) {
            ticket.setStatus(TicketStatus.EM_ESPERA);
            ticketRepository.save(ticket);
        }

        List<Ticket> waitingTickets = ticketRepository.findAllByStatus(TicketStatus.EM_ESPERA);
        for (Ticket ticket : waitingTickets) {
            ticket.setStatus(TicketStatus.EM_ATENDIMENTO);
            ticketRepository.save(ticket);
        }

        List<Ticket> inServiceTickets = ticketRepository.findAllByStatus(TicketStatus.EM_ATENDIMENTO);
        for (Ticket ticket : inServiceTickets) {
            ticket.setStatus(TicketStatus.CONCLUIDO);
            ticketRepository.save(ticket);
        }
    }
}
