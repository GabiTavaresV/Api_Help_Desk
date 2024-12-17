package com.api.helpdesk.repository;

import com.api.helpdesk.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Device d SET d.isDeleted = true WHERE d.id = :id")
    void softDeleteByDeviceId(@Param("id") Long id);

    @Query("SELECT d FROM Device d WHERE d.isDeleted = false")
    List<Device> findAllActiveDevices();
}
