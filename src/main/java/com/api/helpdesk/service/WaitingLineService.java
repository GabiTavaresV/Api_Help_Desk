package com.api.helpdesk.service;

import com.api.helpdesk.dto.DeskDTO;
import com.api.helpdesk.dto.TicketRequest;
import com.api.helpdesk.entity.WaitingLine;
import com.api.helpdesk.repository.WaitingLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaitingLineService {

    @Autowired
    private WaitingLineRepository waitingTicketRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private DeskService deskService;

    @Scheduled(fixedRate = 180000)
    public void processWaitingTickets() {
        List<WaitingLine> waitingTickets = waitingTicketRepository.findAll();

        for (WaitingLine waitingTicket : waitingTickets) {

            List<DeskDTO> availableDesks = deskService.findAvailableDesks();

            if (!availableDesks.isEmpty()) {
                DeskDTO assignedDesk = availableDesks.get(0);

                TicketRequest ticketRequest = new TicketRequest();
                ticketRequest.setCustomerId(waitingTicket.getCustomerId());
                ticketRequest.setDeviceId(waitingTicket.getDeviceId());
                ticketRequest.setReason(waitingTicket.getReason());

                ticketService.createTicket(ticketRequest);

                waitingTicketRepository.delete(waitingTicket);
            }
        }
    }
}
