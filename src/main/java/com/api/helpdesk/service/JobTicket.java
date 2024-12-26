package com.api.helpdesk.service;


import com.api.helpdesk.entity.Ticket;
import com.api.helpdesk.repository.TicketRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.api.helpdesk.utils.TicketStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobTicket {

    private final TicketRepository ticketRepository;

    public JobTicket(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void processTickets() {
        List<Ticket> tickets = ticketRepository.findAll();

        for (Ticket ticket : tickets) {
            LocalDateTime now = LocalDateTime.now();

            if (ticket.getStatus() == TicketStatus.ABERTO && isOlderThan(ticket.getUpdatedAt(), 30)) {
                ticket.setStatus(TicketStatus.EM_ESPERA);
                ticket.setUpdatedAt(now);
                ticketRepository.save(ticket);
            }

            else if (ticket.getStatus() == TicketStatus.EM_ESPERA && isOlderThan(ticket.getUpdatedAt(), 30)) {
                ticket.setStatus(TicketStatus.EM_ATENDIMENTO);
                ticket.setUpdatedAt(now);
                ticketRepository.save(ticket);
            }

            else if (ticket.getStatus() == TicketStatus.EM_ATENDIMENTO && isOlderThan(ticket.getUpdatedAt(), 30)) {
                ticket.setStatus(TicketStatus.CONCLUIDO);
                ticket.setUpdatedAt(now);
                ticketRepository.save(ticket);
            }
        }
    }

    private boolean isOlderThan(LocalDateTime dateTime, int seconds) {
        return dateTime.plusSeconds(seconds).isBefore(LocalDateTime.now());
    }
}
