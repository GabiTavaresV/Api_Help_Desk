package com.api.helpdesk.dto;

import com.api.helpdesk.utils.TicketStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TicketDTO {

    private Long id;
    private UserDTO customer;
    private DeskDTO desk;
    private DeviceDTO device;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedDate;
    private String reason;
    private TicketStatus status;
    private Boolean isDeleted = false;
    private LocalDateTime updatedAt;
}
