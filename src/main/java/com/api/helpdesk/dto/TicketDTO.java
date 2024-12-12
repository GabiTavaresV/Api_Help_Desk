package com.api.helpdesk.dto;

import com.api.helpdesk.utils.TicketStatus;

public class TicketDTO {

    private Long id;
    private String reason;
    private TicketStatus status;
    private UserDTO customer;
    private DeviceDTO device;
    private AttendantDTO attendant;
    private DeskDTO desk;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UserDTO getCustomer() {
        return customer;
    }

    public void setCustomer(UserDTO customer) {
        this.customer = customer;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public DeviceDTO getDevice() {
        return device;
    }

    public void setDevice(DeviceDTO device) {
        this.device = device;
    }

    public AttendantDTO getAttendant() {
        return attendant;
    }

    public void setAttendant(AttendantDTO attendant) {
        this.attendant = attendant;
    }

    public DeskDTO getDesk() {
        return desk;
    }

    public void setDesk(DeskDTO desk) {
        this.desk = desk;
    }
}
