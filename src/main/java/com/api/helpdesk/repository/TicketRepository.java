package com.api.helpdesk.repository;

import com.api.helpdesk.entity.Ticket;
import com.api.helpdesk.utils.TicketStatus;
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

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.desk.id = :deskId AND t.status <> :status")
    long countTicketsByDeskIdAndStatusNot(@Param("deskId") Long deskId, @Param("status") TicketStatus status);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status ='CONCLUIDO'")
    long countTicketsByDeskId();

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.desk.id = :deskId AND t.status <> :status")
    long countOpenTicketsByDeskId(@Param("deskId") Long deskId, @Param("status") TicketStatus status);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.customer.id = :customerId AND t.device.serialNumber = :serialNumber AND t.status = :status")
    long countActiveTicketsByCustomerAndSerialNumber(@Param("customerId") Long customerId, @Param("serialNumber") String serialNumber, @Param("status") TicketStatus status);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.device.serialNumber = :serialNumber AND t.status = :status")
    long countActiveTicketsBySerialNumber(@Param("serialNumber") String serialNumber, @Param("status") TicketStatus status);

}

