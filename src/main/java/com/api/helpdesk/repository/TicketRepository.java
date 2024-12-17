package com.api.helpdesk.repository;

import com.api.helpdesk.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Page<Ticket> findByCustomerId(Long customerId, Pageable pageable);
    Page<Ticket> findByDeskId(Long deskId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Ticket t SET t.isDeleted = true WHERE t.id = :id")
    void softDeleteTicketById(@Param("id") Long id);

    @Query("SELECT t FROM Ticket t WHERE t.isDeleted = false")
    List<Ticket> findAllActiveTickets();
}
