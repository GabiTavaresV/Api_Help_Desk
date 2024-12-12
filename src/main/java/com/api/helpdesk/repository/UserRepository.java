package com.api.helpdesk.repository;

import com.api.helpdesk.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

}