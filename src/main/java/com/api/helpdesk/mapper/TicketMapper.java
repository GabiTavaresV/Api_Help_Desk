package com.api.helpdesk.mapper;

import com.api.helpdesk.dto.TicketDTO;
import com.api.helpdesk.entity.Ticket;

public class TicketMapper {

    private final UserMapper userMapper = new UserMapper();
    private final DeskMapper deskMapper = new DeskMapper();
    private final DeviceMapper deviceMapper = new DeviceMapper();
    private final AttendantMapper attendantMapper = new AttendantMapper();

    public TicketDTO toDTO(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setReason(ticket.getReason());
        ticketDTO.setStatus(ticket.getStatus());

        if (ticket.getCustomer() != null) {
            ticketDTO.setCustomer(userMapper.toDTO(ticket.getCustomer()));
        }
        if (ticket.getDevice() != null) {
            ticketDTO.setDevice(deviceMapper.toDTO(ticket.getDevice()));
        }

        if (ticket.getDesk() != null) {
            ticketDTO.setDesk(deskMapper.toDTO(ticket.getDesk()));
        }

        if (ticket.getAttendant() != null) {
            ticketDTO.setAttendant(attendantMapper.toDTO(ticket.getAttendant()));
        }
        return ticketDTO;
    }

    public Ticket toEntity(TicketDTO ticketDTO) {
        if (ticketDTO == null) {
            return null;
        }
        Ticket ticket = new Ticket();
        ticket.setId(ticketDTO.getId());
        ticket.setReason(ticketDTO.getReason());
        ticket.setStatus(ticketDTO.getStatus());

        if (ticketDTO.getCustomer() != null) {
            ticket.setCustomer(userMapper.toEntity(ticketDTO.getCustomer()));
        }

        if (ticketDTO.getDevice() != null) {
            ticket.setDevice(deviceMapper.toEntity(ticketDTO.getDevice()));
        }

        if (ticketDTO.getDesk() != null) {
            ticket.setDesk(deskMapper.toEntity(ticketDTO.getDesk()));
        }

        return ticket;
    }
}
