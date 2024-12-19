package com.api.helpdesk.entity;

import com.api.helpdesk.utils.TicketStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ticket")

public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    @NotNull(message = "O id do usuário não pode ser nulo.")
    @NotBlank(message = "O id do usuário não pode ser vazio.")
    private Users customer;

    @ManyToOne
    @JoinColumn(name = "desk_id")
    @NotNull(message = "O id do balcão não pode ser nulo.")
    @NotBlank(message = "O id do balcão não pode ser vazio.")
    private Desk desk;

    @ManyToOne
    @JoinColumn(name = "device_id")
    @NotNull(message = "O id do equipamento não pode ser nulo.")
    @NotBlank(message = "O id do equipamento não pode ser vazio.")
    private Device device;

    @ManyToOne
    @JoinColumn(name = "attendant_id")
    @NotNull(message = "O id do atendente não pode ser nulo.")
    @NotBlank(message = "O id do atendente não pode ser vazio.")
    private Attendant attendant;

    private LocalDateTime createdDate;

    @Column(name = "resolved_date")
    private LocalDateTime resolvedDate;

    @NotNull(message = "O motivo não pode ser nulo.")
    @NotBlank(message = "O motivo não pode ser vazio.")
    private String reason;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Column(nullable = false)
    private boolean isDeleted = false;
}