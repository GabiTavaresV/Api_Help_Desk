package com.api.helpdesk.dto;

public class DeviceDTO {

    private Long id;
    private String serialNumber;


    public DeviceDTO() {
        this.id = id;
    }

    public DeviceDTO(Long id, String serialNumber) {
        this.id = id;
        this.serialNumber = serialNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
