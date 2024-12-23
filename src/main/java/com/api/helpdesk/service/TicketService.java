package com.api.helpdesk.service;

import com.api.helpdesk.dto.*;
import com.api.helpdesk.entity.Ticket;
import com.api.helpdesk.exception.ConflictException;
import com.api.helpdesk.exception.ForbiddenException;
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

    public TicketDTO createTicket(Long customerId, Long deskId, Long deviceId, String reason) throws NotFoundDBException {
        UserDTO user = userService.getUserById(customerId);
        DeskDTO desk = deskService.getDeskById(deskId);
        DeviceDTO device = deviceService.getDeviceById(deviceId);

        String serialNumber = device.getSerialNumber();

        long activeTicketsCount = ticketRepository.countActiveTicketsBySerialNumber(serialNumber, TicketStatus.ABERTO);

        if (desk.getAttendant() == null) {
            throw new NotFoundDBException("Desk does not have an attendant associated.");
        }


        if (activeTicketsCount > 0) {
            throw new ForbiddenException("Outro chamado já está em atendimento para o serial number: " + serialNumber);
        }

        long userActiveTicketsCount = ticketRepository.countActiveTicketsByCustomerAndSerialNumber(customerId, serialNumber, TicketStatus.ABERTO);

        if (userActiveTicketsCount > 0) {
            throw new ConflictException("O usuário já possui um chamado aberto para o mesmo serial number: " + serialNumber );
        }

        long openTicketsCount = ticketRepository.countOpenTicketsByDeskId(deskId, TicketStatus.CONCLUIDO);
        if (openTicketsCount >= 5) {
            throw new IllegalStateException("Não é possível criar mais de 5 chamados abertos para este balcão.");
        }

        Ticket ticket = new Ticket();
        ticket.setCustomer(userMapper.toEntity(user));
        ticket.setDesk(deskMapper.toEntity(desk));
        ticket.setDevice(deviceMapper.toEntity(device));
        ticket.setReason(reason);
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

    public void updateStatusById(Long id, TicketStatus status) throws NotFoundDBException {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundDBException("Ticket not found"));

        ticket.setStatus(status);

        if (status == TicketStatus.CONCLUIDO) {
            ticket.setResolvedDate(LocalDateTime.now());
        }

        ticketRepository.save(ticket);
    }

}