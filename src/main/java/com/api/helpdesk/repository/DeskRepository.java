package com.api.helpdesk.repository;

import com.api.helpdesk.entity.Desk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DeskRepository extends JpaRepository<Desk, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Desk dk SET dk.isDeleted = true WHERE dk.id = :id")
    void softDeleteDeskById(@Param("id") Long id);

    @Query("SELECT dk FROM Desk dk WHERE dk.isDeleted = false")
    List<Desk> findAllActiveDesks();
}
