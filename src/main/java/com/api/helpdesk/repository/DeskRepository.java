package com.api.helpdesk.repository;

import com.api.helpdesk.entity.Desk;
import com.api.helpdesk.utils.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeskRepository extends JpaRepository<Desk, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Desk dk SET dk.isDeleted = true WHERE dk.id = :id")
    void softDeleteDeskById(@Param("id") Long id);

    @Query("SELECT dk FROM Desk dk WHERE dk.isDeleted = false")
    List<Desk> findAllActiveDesks();

    @Query("SELECT dk FROM Desk dk WHERE dk.id = :id AND dk.isDeleted = false")
    Optional<Desk> findActiveDeskById(@Param("id") Long id);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.desk.id = :deskId AND t.status <> :status")
    long countOpenTicketsByDeskId(@Param("deskId") Long deskId, @Param("status") TicketStatus status);

    @Query("SELECT d FROM Desk d WHERE d.attendant IS NOT NULL")
    List<Desk> findAllWithAttendant();
}