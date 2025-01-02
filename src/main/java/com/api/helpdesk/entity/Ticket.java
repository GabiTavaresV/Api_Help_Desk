package com.api.helpdesk.entity;

import com.api.helpdesk.utils.TicketStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users customer;

    @ManyToOne
    @JoinColumn(name = "desk_id")
    private Desk desk;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "resolved_date")
    private LocalDateTime resolvedDate;

    private String reason;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}