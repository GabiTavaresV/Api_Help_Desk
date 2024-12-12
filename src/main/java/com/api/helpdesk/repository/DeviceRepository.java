package com.api.helpdesk.repository;

import com.api.helpdesk.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {}
