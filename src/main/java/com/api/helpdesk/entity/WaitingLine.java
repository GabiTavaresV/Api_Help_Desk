package com.api.helpdesk.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "waiting_ticket")
public class WaitingLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private Long deviceId;
    private String reason;

    private LocalDateTime requestTime;
}
