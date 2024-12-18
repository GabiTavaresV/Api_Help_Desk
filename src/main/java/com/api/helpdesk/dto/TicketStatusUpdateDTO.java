package com.api.helpdesk.dto;

import com.api.helpdesk.utils.TicketStatus;
import lombok.*;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketStatusUpdateDTO {
    private TicketStatus status;
}
