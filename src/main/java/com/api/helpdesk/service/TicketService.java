package com.api.helpdesk.service;

import com.api.helpdesk.dto.*;
import com.api.helpdesk.entity.Ticket;
import com.api.helpdesk.mapper.*;
import com.api.helpdesk.repository.TicketRepository;
import com.api.helpdesk.utils.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.api.helpdesk.exception.NotFoundDBException;

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

    @Autowired
    private AttendantService attendantService;

    private final TicketMapper ticketMapper = new TicketMapper();
    private final DeskMapper deskMapper = new DeskMapper();
    private final UserMapper userMapper = new UserMapper();
    private final DeviceMapper deviceMapper = new DeviceMapper();
    private final AttendantMapper attendantMapper = new AttendantMapper();

    public TicketDTO createTicket(Long customerId, Long deskId, Long deviceId, String reason, Long attendantId) throws NotFoundDBException {

        UserDTO user = userService.getUserById(customerId);
        DeskDTO desk = deskService.getDeskById(deskId);
        DeviceDTO device = deviceService.getDeviceById(deviceId);
        AttendantDTO attendant = attendantService.getAttendantById(attendantId);

        Ticket ticket = new Ticket();
        ticket.setCustomer(userMapper.toEntity(user));
        ticket.setDesk(deskMapper.toEntity(desk));
        ticket.setDevice(deviceMapper.toEntity(device));
        ticket.setReason(reason);
        ticket.setAttendant(attendantMapper.toEntity(attendant));

        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setStatus(TicketStatus.ABERTO);

        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toDTO(savedTicket);
    }

    public List<Ticket> listAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable).getContent();
    }

    public Page<Ticket> listTicketsByCustomerId(Long customerId, Pageable pageable) {
        return ticketRepository.findByCustomerId(customerId, pageable);
    }

    public Ticket getTicketDetails(Long id) throws NotFoundDBException {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundDBException("Chamado não encontrado!"));
        return ticket;
    }

    public Page<Ticket> listTicketsByDeskId(Long deskId, Pageable pageable) {
        return ticketRepository.findByDeskId(deskId, pageable);
    }

    public Void deleteTicketById(Long id) throws NotFoundDBException {
        ticketRepository.softDeleteTicketById(id);
        return null;
    }

}