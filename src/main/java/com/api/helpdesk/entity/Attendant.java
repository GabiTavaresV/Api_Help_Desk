package com.api.helpdesk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attendant")
public class Attendant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O nome não pode ser nulo.")
    @NotBlank(message = "O nome não pode ser vazio.")
    private String name;

    @OneToMany(mappedBy = "attendant")
    @JsonIgnore
    private List<Desk> desks = new ArrayList<>();

    @Column(nullable = false)
    private boolean isDeleted = false;
}
