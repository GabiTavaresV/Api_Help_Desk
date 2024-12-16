package com.api.helpdesk.dto;

import lombok.*;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TicketRequest {
    private Long customerId;
    private Long deskId;
    private Long deviceId;
    private String reason;
    private Long attendantId;

}
