package com.api.helpdesk.controller;

import com.api.helpdesk.entity.Attendant;
import com.api.helpdesk.entity.Desk;
import com.api.helpdesk.entity.Users;
import com.api.helpdesk.service.AttendantService;
import com.api.helpdesk.service.DeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/desk")
public class DeskController {

    @Autowired
    private DeskService deskService;

    @Autowired
    private AttendantService attendantService;

    @PostMapping
    public Desk create(@RequestBody Desk desk) {
       return deskService.register(desk);
    }

    @GetMapping("/findAll")
    public List<Desk> getAll() {
        return deskService.getAllDesks();
    }
}
