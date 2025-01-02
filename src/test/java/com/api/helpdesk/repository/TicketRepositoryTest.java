package com.api.helpdesk.repository;

import com.api.helpdesk.entity.*;
import com.api.helpdesk.utils.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TicketRepositoryTest {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private DeskRepository deskRepository;

    private Users customer;
    private Device device;
    private Ticket ticket;
    private Desk desk;
    private Attendant attendant;

    @BeforeEach
    public void setUp() {
        customer = new Users();
        customer.setName("Test Customer");
        customer.setEmail("customer@example.com");
        usersRepository.save(customer);

        device = new Device();
        device.setSerialNumber("SN12345");
        deviceRepository.save(device);

        ticket = new Ticket();
        ticket.setCustomer(customer);
        ticket.setDevice(device);
        ticket.setStatus(TicketStatus.ABERTO);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setIsDeleted(false);
        ticketRepository.save(ticket);

        desk = new Desk();
        desk.setAttendant(attendant);
    }

    @Test
    public void whenFindByCustomerId_thenReturnTickets() {
        Page<Ticket> tickets = ticketRepository.findByCustomerId(customer.getId(), PageRequest.of(0, 10));
        assertThat(tickets.getContent()).contains(ticket);
    }

    @Test
    public void whenFindByDeskId_thenReturnTickets() {
        // Para este teste, você pode precisar criar um Desk para associar ao ticket
        // O código depende de como a sua entidade Ticket é implementada
    }

    @Test
    public void whenSoftDeleteTicketById_thenTicketIsDeleted() {
        ticketRepository.softDeleteTicketById(ticket.getId());
        Ticket deletedTicket = ticketRepository.findById(ticket.getId()).orElse(null);
        assertThat(deletedTicket).isNotNull();
        assertTrue(deletedTicket.getIsDeleted());
    }

    @Test
    public void whenCountOpenTicketsByDeskId_thenReturnCount() {
        long count = ticketRepository.countOpenTicketsByDeskId(desk.getId(), TicketStatus.ABERTO);
        assertEquals(1, count);
    }

    @Test
    public void whenCountActiveTicketsByCustomerAndSerialNumber_thenReturnCount() {
        long count = ticketRepository.countActiveTicketsByCustomerAndSerialNumber(customer.getId(), device.getSerialNumber(), TicketStatus.ABERTO);
        assertEquals(1, count);
    }

}

