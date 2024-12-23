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
    private TicketService ticketService; // Para criar novos tickets

    @Autowired
    private DeskService deskService;

    @Scheduled(fixedRate = 180000) // Executa a cada 3 minutos
    public void processWaitingTickets() {
        List<WaitingLine> waitingTickets = waitingTicketRepository.findAll(); // Obter todos os tickets em espera

        for (WaitingLine waitingTicket : waitingTickets) {
            // Aqui você chamaria a lógica para criar um novo ticket
            // Primeiro, verifique os balcões disponíveis
            List<DeskDTO> availableDesks = deskService.findAvailableDesks();

            if (!availableDesks.isEmpty()) {
                DeskDTO assignedDesk = availableDesks.get(0); // Atribuir o primeiro balcão disponível

                // Criar o ticket a partir do waiting ticket
                TicketRequest ticketRequest = new TicketRequest();
                ticketRequest.setCustomerId(waitingTicket.getCustomerId());
                ticketRequest.setDeviceId(waitingTicket.getDeviceId());
                ticketRequest.setReason(waitingTicket.getReason());

                // Criar o ticket
                ticketService.createTicket(ticketRequest);

                // Após criar, pode-se remover da lista de tickets de espera
                waitingTicketRepository.delete(waitingTicket); // Remover o ticket da espera, se necessário
            }
        }
    }
}
