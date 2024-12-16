package com.api.helpdesk.dto;

import com.api.helpdesk.utils.TicketStatus;
import lombok.*;


@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TicketDTO {

    private Long id;
    private String reason;
    private TicketStatus status;
    private UserDTO customer;
    private DeviceDTO device;
    private DeskDTO desk;
    private AttendantDTO attendant;

}
