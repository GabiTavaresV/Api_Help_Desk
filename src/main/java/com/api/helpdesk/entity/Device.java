package com.api.helpdesk.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "device")
@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O serial number não pode ser nulo.")
    @NotBlank(message = "O serial number não pode ser vazio.")
    private String serialNumber;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}
