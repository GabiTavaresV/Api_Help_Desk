package com.api.helpdesk.dto;

import lombok.*;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AttendantDTO {
    private Long id;
    private String name;
}
