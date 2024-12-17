package com.api.helpdesk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "desk")
public class Desk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attendant_id")
    private Attendant attendant;

    @OneToMany(mappedBy = "desk")
    @JsonIgnore
    private List<Ticket> tickets = new ArrayList<>();

    @Column(nullable = false)
    private boolean isDeleted = false;
}
