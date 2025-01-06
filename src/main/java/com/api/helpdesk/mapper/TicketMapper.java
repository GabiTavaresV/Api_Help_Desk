package com.api.helpdesk.mapper;

import org.springframework.stereotype.Component;

import com.api.helpdesk.dto.TicketDTO;
import com.api.helpdesk.entity.Ticket;

import java.time.LocalDateTime;

@Component
public class TicketMapper {

     UserMapper userMapper;
    DeskMapper deskMapper;
    DeviceMapper deviceMapper;

    public TicketDTO toDTO(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setReason(ticket.getReason());
        ticketDTO.setStatus(ticket.getStatus());
        ticketDTO.setIsDeleted(ticket.getIsDeleted());
        ticketDTO.setCreatedAt(ticket.getCreatedAt());
        ticketDTO.setUpdatedAt(ticket.getUpdatedAt());

        if (ticket.getCustomer() != null) {
            ticketDTO.setCustomer(userMapper.toDTO(ticket.getCustomer()));
        }
        if (ticket.getDevice() != null) {
            ticketDTO.setDevice(deviceMapper.toDto(ticket.getDevice()));
        }

        if (ticket.getDesk() != null) {
            ticketDTO.setDesk(deskMapper.deskToDeskDTO(ticket.getDesk()));
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
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setIsDeleted(ticketDTO.getIsDeleted()!= null ? ticketDTO.getIsDeleted() : false);

        if (ticketDTO.getCustomer() != null) {
            ticket.setCustomer(userMapper.toEntity(ticketDTO.getCustomer()));
        }

        if (ticketDTO.getDevice() != null) {
            ticket.setDevice(deviceMapper.toEntity(ticketDTO.getDevice()));
        }

        if (ticketDTO.getDesk() != null) {
            ticket.setDesk(deskMapper.deskDTOToDesk(ticketDTO.getDesk()));
        }

        return ticket;
    }
}
