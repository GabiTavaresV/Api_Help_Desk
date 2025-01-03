package com.api.helpdesk.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.api.helpdesk.entity.Attendant;

@Repository
public interface AttendantRepository extends JpaRepository<Attendant, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Attendant at SET at.isDeleted = true WHERE at.id = :id")
    void softDeleteAttendantById(@Param("id") Long id);

    @Query("SELECT at FROM Attendant at WHERE at.isDeleted = false")
    Page<Attendant> findAllActiveAttendants(Pageable pageable);

    @Query("SELECT COUNT(at) > 0 FROM Attendant at WHERE at.name = :name AND at.isDeleted = false")
    boolean existsByName(@Param("name") String email);

    @Query("SELECT at FROM Attendant at WHERE at.id = :id AND at.isDeleted = false")
    Optional<Attendant> findActiveAttendantById(@Param("id") Long id);

    @Query(value = "SELECT * FROM attendant WHERE id = :attendantId AND is_deleted = true", nativeQuery = true)
    Optional<Attendant> findDeletedAttendantById(@Param("attendantId") Long attendantId);
}
