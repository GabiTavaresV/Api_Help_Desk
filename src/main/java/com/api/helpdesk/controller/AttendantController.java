package com.api.helpdesk.controller;

import com.api.helpdesk.dto.AttendantDTO;
import com.api.helpdesk.service.AttendantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/attendant")
public class AttendantController {

    @Autowired
    private AttendantService attendantService;

    @PostMapping
    public ResponseEntity<AttendantDTO> create(@RequestBody AttendantDTO attendant) {
        AttendantDTO createUser = attendantService.register(attendant);
        return new ResponseEntity<>(createUser, HttpStatus.CREATED);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<AttendantDTO>> getAll() {
        List<AttendantDTO> list = attendantService.getAllAttendants();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendantDTO> getById(@PathVariable Long id) {
        AttendantDTO attendant = attendantService.getAttendantById(id);
        return new ResponseEntity<>(attendant, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        attendantService.deleteAttendantById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
