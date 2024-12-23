package com.api.helpdesk.repository;

import com.api.helpdesk.entity.WaitingLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingLineRepository extends JpaRepository<WaitingLine, Long> {
}
