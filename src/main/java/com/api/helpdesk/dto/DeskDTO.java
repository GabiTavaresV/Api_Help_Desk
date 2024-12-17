package com.api.helpdesk.dto;

import lombok.*;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DeskDTO {
    private Long id;
    private AttendantDTO attendant;
}
