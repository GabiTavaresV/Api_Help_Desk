package com.api.helpdesk.controller;


import com.api.helpdesk.entity.Attendant;
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
    public Attendant create(@RequestBody Attendant attendant) {
        return attendantService.register(attendant);
    }

    @GetMapping("/findAll")
    public List<Attendant> getAll() {
        return attendantService.getAllAttendants();
    }

    @GetMapping("/{id}")
    public Attendant getById(@PathVariable Long id) {
        return attendantService.getAttendantById(id);
    }
}
