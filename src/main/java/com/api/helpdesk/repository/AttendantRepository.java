package com.api.helpdesk.repository;

import com.api.helpdesk.entity.Attendant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AttendantRepository extends JpaRepository<Attendant, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Attendant at SET at.isDeleted = true WHERE at.id = :id")
    void softDeleteAttendantById(@Param("id") Long id);

    @Query("SELECT at FROM Attendant at WHERE at.isDeleted = false")
    List<Attendant> findAllActiveAttendants();
}
