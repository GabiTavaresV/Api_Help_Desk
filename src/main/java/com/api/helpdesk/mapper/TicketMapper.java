package com.api.helpdesk.mapper;

import com.api.helpdesk.dto.TicketDTO;
import com.api.helpdesk.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = { UserMapper.class, DeviceMapper.class, DeskMapper.class })
public interface TicketMapper {

    UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    DeviceMapper deviceMapper = Mappers.getMapper(DeviceMapper.class);
    DeskMapper deskMapper = Mappers.getMapper(DeskMapper.class);

    @Mapping(source = "customer", target = "customer")
    @Mapping(source = "device", target = "device")
    @Mapping(source = "desk", target = "desk")
    TicketDTO toDTO(Ticket ticket);

    @Mapping(source = "customer", target = "customer")
    @Mapping(source = "device", target = "device")
    @Mapping(source = "desk", target = "desk")

    default Ticket toEntity(TicketDTO ticketDTO) {
        if (ticketDTO == null) {
            return null;
        }

        Ticket ticket = new Ticket();
        ticket.setId(ticketDTO.getId());
        ticket.setReason(ticketDTO.getReason());
        ticket.setStatus(ticketDTO.getStatus());
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setCreatedAt(LocalDateTime.now());

        ticket.setIsDeleted(ticketDTO.getIsDeleted() != null ? ticketDTO.getIsDeleted() : false);

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

