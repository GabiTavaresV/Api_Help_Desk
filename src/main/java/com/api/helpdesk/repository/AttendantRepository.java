package com.api.helpdesk.repository;

import com.api.helpdesk.entity.Attendant;
import com.api.helpdesk.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendantRepository extends JpaRepository<Attendant, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Attendant at SET at.isDeleted = true WHERE at.id = :id")
    void softDeleteAttendantById(@Param("id") Long id);

    @Query("SELECT at FROM Attendant at WHERE at.isDeleted = false")
    List<Attendant> findAllActiveAttendants();

    @Query("SELECT COUNT(at) > 0 FROM Attendant at WHERE at.name = :name AND at.isDeleted = false")
    boolean existsByName(@Param("name") String email);

    @Query("SELECT at FROM Attendant at WHERE at.id = :id AND at.isDeleted = false")
    Optional<Attendant> findActiveAttendantById(@Param("id") Long id);
}
