package com.api.helpdesk.repository;

import com.api.helpdesk.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Page<Ticket> findByCustomerId(Long customerId, Pageable pageable);
    List<Ticket> findByDeskId(Long deskId);
    Page<Ticket> findByDeskId(Long deskId, Pageable pageable);

//    boolean existsByDeviceAndStatusIn(Device device, List<TicketStatus> statuses);

}
