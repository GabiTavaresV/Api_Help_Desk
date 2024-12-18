package com.api.helpdesk.controller;

import com.api.helpdesk.dto.TicketDTO;
import com.api.helpdesk.dto.TicketRequest;
import com.api.helpdesk.dto.TicketStatusUpdateDTO;
import com.api.helpdesk.entity.Ticket;
import com.api.helpdesk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public TicketDTO create(@RequestBody TicketRequest ticketRequest) {
        return ticketService.createTicket(ticketRequest.getCustomerId(), ticketRequest.getDeskId(), ticketRequest.getDeviceId(), ticketRequest.getReason(), ticketRequest.getAttendantId());
    }

    @GetMapping("/findAll")
    public List<Ticket> getAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ticketService.listAllTickets(pageable);
    }

    @GetMapping("/{id}")
    public Ticket getById(@PathVariable Long id) {
        return ticketService.getTicketDetails(id);
    }

    @GetMapping("/desk/{deskId}")
    public ResponseEntity<Page<Ticket>> getTicketsByDesk(@PathVariable Long deskId, @PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<Ticket> tickets = ticketService.listTicketsByDeskId(deskId, pageable);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<Ticket>> getTicketsByCustomerId(@PathVariable Long customerId, @PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<Ticket> tickets = ticketService.listTicketsByCustomerId(customerId, pageable);
        return ResponseEntity.ok(tickets);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ticketService.deleteTicketById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody TicketStatusUpdateDTO statusUpdate) {
        ticketService.updateStatusById(id, statusUpdate.getStatus());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
