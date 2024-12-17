package com.api.helpdesk.repository;

import com.api.helpdesk.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Page<Ticket> findByCustomerId(Long customerId, Pageable pageable);
    Page<Ticket> findByDeskId(Long deskId, Pageable pageable);
}
