package com.api.helpdesk.dto;

import lombok.*;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DeviceDTO {

    private Long id;
    private String serialNumber;
}
