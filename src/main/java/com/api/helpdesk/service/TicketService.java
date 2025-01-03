package com.api.helpdesk.service;

import com.api.helpdesk.dto.*;
import com.api.helpdesk.entity.Ticket;
import com.api.helpdesk.entity.WaitingLine;
import com.api.helpdesk.exception.ConflictException;
import com.api.helpdesk.exception.ForbiddenException;
import com.api.helpdesk.exception.InputRequiredException;
import com.api.helpdesk.mapper.*;
import com.api.helpdesk.repository.TicketRepository;
import com.api.helpdesk.repository.WaitingLineRepository;
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
    private WaitingLineRepository waitingLineRepository;

    @Autowired
    private DeskService deskService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    private TicketMapper ticketMapper = new TicketMapper();

    public TicketDTO createTicket(TicketRequest ticketRequest) throws NotFoundDBException {
        validateTicketRequest(ticketRequest);
        Long customerId = ticketRequest.getCustomerId();
        Long deviceId = ticketRequest.getDeviceId();
        String reason = ticketRequest.getReason();

        UserDTO user = userService.getUserById(customerId);
        DeviceDTO device = deviceService.getDeviceById(deviceId);

        ensureNoActiveTicketForSerialNumber(device.getSerialNumber(), customerId);

        DeskDTO assignedDesk = findAvailableDesk();
        if (assignedDesk == null) {
            saveWaitingLine(customerId, deviceId, reason);
            return null;
        }

        TicketDTO ticketDTO = createNewTicket(user, device, assignedDesk, reason);

        Ticket ticket = ticketMapper.toEntity(ticketDTO);

        Ticket savedTicket = ticketRepository.save(ticket);

        return ticketMapper.toDTO(savedTicket);
    }

    private void validateTicketRequest(TicketRequest ticketRequest) {
        if (ticketRequest.getCustomerId() == null || ticketRequest.getDeviceId() == null || ticketRequest.getReason() == null) {
            throw new InputRequiredException("Id do Cliente, Id do Aparelho e motivo do chamado são obrigatórios!.");
        }
    }

    private void ensureNoActiveTicketForSerialNumber(String serialNumber, Long customerId) {

        if (ticketRepository.countActiveTicketsByCustomerAndSerialNumber(customerId, serialNumber, TicketStatus.ABERTO) > 0) {
            throw new ConflictException("O usuário já possui um chamado aberto para o mesmo serial number: " + serialNumber);
        }
        if (ticketRepository.countActiveTicketsBySerialNumberNotConcluded(serialNumber, TicketStatus.CONCLUIDO) > 0) {
            throw new ForbiddenException("Outro chamado já está em atendimento para o serial number: " + serialNumber);
        }

    }

    private DeskDTO findAvailableDesk() {
        List<DeskDTO> availableDesks = deskService.findAvailableDesks();

        for (DeskDTO desk : availableDesks) {
            long nonConcludedTicketsCount = ticketRepository.countTicketsByDeskIdAndNotStatus(desk.getId(), TicketStatus.CONCLUIDO);

            if (nonConcludedTicketsCount < 5) {
                return desk;
            }
        }
        return null;
    }

    private void saveWaitingLine(Long customerId, Long deviceId, String reason) {
        WaitingLine waitingTicket = WaitingLine.builder()
                .customerId(customerId)
                .deviceId(deviceId)
                .reason(reason)
                .requestTime(LocalDateTime.now())
                .build();

        waitingLineRepository.save(waitingTicket);
    }

    private TicketDTO createNewTicket(UserDTO user, DeviceDTO device, DeskDTO desk, String reason) {
        return TicketDTO.builder()
                .customer(user)
                .device(device)
                .desk(desk)
                .reason(reason)
                .createdAt(LocalDateTime.now())
                .status(TicketStatus.ABERTO)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Page<Ticket> listAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable);
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