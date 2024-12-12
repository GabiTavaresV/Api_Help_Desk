package com.api.helpdesk.service;


import com.api.helpdesk.dto.TicketDTO;
import com.api.helpdesk.dto.UserDTO;
import com.api.helpdesk.entity.Desk;
import com.api.helpdesk.entity.Device;
import com.api.helpdesk.entity.Ticket;
import com.api.helpdesk.entity.Users;
import com.api.helpdesk.repository.TicketRepository;
import com.api.helpdesk.utils.TicketStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private DeskService deskService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    public Ticket createTicket(Long customerId, Long deskId, Long deviceId, String reason) {
        Users user = userService.getUserById(customerId);
        Desk desk = deskService.getDeskById(deskId);
        Device device = deviceService.getDeviceById(deviceId);

        Ticket ticket = new Ticket();
        ticket.setCustomer(user);
        ticket.setDesk(desk);
        ticket.setDevice(device);
        ticket.setReason(reason);
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setStatus(TicketStatus.ABERTO);

        return ticketRepository.save(ticket);
    }

    public List<Ticket> listAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable).getContent();
    }

    public Page<Ticket> listTicketsByCustomerId(Long customerId, Pageable pageable) {
        return ticketRepository.findByCustomerId(customerId, pageable);
    }

    public TicketDTO getTicketDetails(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setReason(ticket.getReason());
        ticketDTO.setStatus(ticket.getStatus());

        if (ticket.getCustomer() != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(ticket.getCustomer().getId());
            userDTO.setName(ticket.getCustomer().getName());
            userDTO.setEmail(ticket.getCustomer().getEmail());

            ticketDTO.setCustomer(userDTO);
        }

        return ticketDTO;
    }

    public Page<Ticket> listTicketsByDeskId(Long deskId, Pageable pageable) {
        return ticketRepository.findByDeskId(deskId, pageable);
    }

}