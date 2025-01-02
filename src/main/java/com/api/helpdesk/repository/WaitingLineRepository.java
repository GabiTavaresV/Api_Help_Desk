package com.api.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.helpdesk.entity.WaitingLine;

@Repository
public interface WaitingLineRepository extends JpaRepository<WaitingLine, Long> {
}
