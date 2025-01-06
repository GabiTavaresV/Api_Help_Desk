
package com.api.helpdesk.repository;

import com.api.helpdesk.entity.Device;
import com.api.helpdesk.entity.Ticket;
import com.api.helpdesk.entity.Users;
import com.api.helpdesk.utils.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TicketRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TicketRepository ticketRepository;

    private Ticket ticket;
    private Users user;
    private Device device;

    @BeforeEach
    public void setUp() {
        user = new Users();
        user.setName("Test User");
        user.setEmail("test@example.com");
        entityManager.persistAndFlush(user);

        device = new Device();
        device.setSerialNumber("ABC123");
        entityManager.persistAndFlush(device);

        ticket = new Ticket();
        ticket.setCustomer(user);
        ticket.setDevice(device);
        ticket.setStatus(TicketStatus.ABERTO);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        entityManager.persistAndFlush(ticket);
    }

    @Test
    public void whenFindByCustomerId_thenReturnTickets() {
        Long customerId = user.getId();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ticket> ticketsPage = ticketRepository.findByCustomerId(customerId, pageable);
        assertThat(ticketsPage.hasContent()).isTrue();
        assertTrue(ticketsPage.getContent().contains(ticket));
        assertThat(ticketsPage.getTotalElements()).isGreaterThan(0);
        assertThat(ticketsPage.getSize()).isLessThanOrEqualTo(10);
    }

    @Test
    public void whenFindByDeskId_thenReturnTickets() {
        Long deskId = ticket.getDesk() != null ? ticket.getDesk().getId() : null;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ticket> ticketsPage = ticketRepository.findByDeskId(deskId, pageable);
        assertThat(ticketsPage.getContent()).isEmpty();
    }

    @Test
    public void whenSoftDeleteTicketById_thenTicketIsDeleted() {
        Long ticketId = ticket.getId();
        ticketRepository.softDeleteTicketById(ticketId);
        Boolean isDeleted = ticketRepository.isTicketDeleted(ticketId);
        assertTrue(isDeleted);
    }

    @Test
    public void whenCountTicketsByDeskIdAndStatusNot_thenReturnCount() {
        Long deskId = ticket.getDesk() != null ? ticket.getDesk().getId() : null;
        long count = ticketRepository.countTicketsByDeskIdAndStatusNot(deskId, TicketStatus.CONCLUIDO);
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void whenCountOpenTicketsByDeskId_thenReturnCountOfOpenTickets() {
        Long deskId = ticket.getDesk() != null ? ticket.getDesk().getId() : null;
        long count = ticketRepository.countOpenTicketsByDeskId(deskId, TicketStatus.ABERTO);
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void whenCountActiveTicketsByCustomerAndSerialNumber_thenReturnCount() {
        long count = ticketRepository.countActiveTicketsByCustomerAndSerialNumber(user.getId(), device.getSerialNumber(), TicketStatus.ABERTO);
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void whenCountActiveTicketsBySerialNumberNotConcluded_thenReturnCount() {
        long count = ticketRepository.countActiveTicketsBySerialNumberNotConcluded(device.getSerialNumber(), TicketStatus.CONCLUIDO);
        assertThat(count).isEqualTo(1);
    }
}