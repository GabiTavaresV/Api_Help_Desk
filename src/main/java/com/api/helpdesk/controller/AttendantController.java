package com.api.helpdesk.controller;


import com.api.helpdesk.dto.AttendantDTO;
import com.api.helpdesk.service.AttendantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/attendant")
public class AttendantController {

    @Autowired
    private AttendantService attendantService;

    @PostMapping
    public AttendantDTO create(@RequestBody AttendantDTO attendant) {
        return attendantService.register(attendant);
    }

    @GetMapping("/findAll")
    public List<AttendantDTO> getAll() {
        return attendantService.getAllAttendants();
    }

    @GetMapping("/{id}")
    public AttendantDTO getById(@PathVariable Long id) {
        return attendantService.getAttendantById(id);
    }
}
