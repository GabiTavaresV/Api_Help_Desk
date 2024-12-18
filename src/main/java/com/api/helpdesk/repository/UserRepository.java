package com.api.helpdesk.repository;

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
public interface UserRepository extends JpaRepository<Users, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.isDeleted = true WHERE u.id = :id")
    void softDeleteUserById(@Param("id") Long id);

    @Query("SELECT u FROM Users u WHERE u.isDeleted = false")
    List<Users> findAllActiveUsers();

    @Query("SELECT u FROM Users u WHERE u.id = :id AND u.isDeleted = false")
    Optional<Users> findActiveUserById(@Param("id") Long id);

    @Query("SELECT COUNT(u) > 0 FROM Users u WHERE u.email = :email AND u.isDeleted = false")
    boolean existsByEmail(@Param("email") String email);

}