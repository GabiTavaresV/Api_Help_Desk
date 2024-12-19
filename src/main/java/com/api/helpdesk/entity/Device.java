package com.api.helpdesk.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O serial number não pode ser nulo.")
    @NotBlank(message = "O serial number não pode ser vazio.")
    private String serialNumber;

    @Column(nullable = false)
    private boolean isDeleted = false;
}
