package com.api.helpdesk.repository;

import com.api.helpdesk.entity.Desk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeskRepository extends JpaRepository<Desk, Long> {}
