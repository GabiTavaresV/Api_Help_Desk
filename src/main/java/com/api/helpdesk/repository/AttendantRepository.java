package com.api.helpdesk.repository;

import com.api.helpdesk.entity.Attendant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendantRepository extends JpaRepository<Attendant, Long> {

}
