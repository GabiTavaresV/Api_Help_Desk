package com.api.helpdesk.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.helpdesk.dto.AttendantDTO;
import com.api.helpdesk.service.AttendantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/attendant")
public class AttendantController {

    private final AttendantService attendantService;

    @PostMapping
    public ResponseEntity<AttendantDTO> create(@Valid @RequestBody AttendantDTO attendant) {
        AttendantDTO createUser = attendantService.register(attendant);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }

    @GetMapping("/findAll")
    public ResponseEntity<Page<AttendantDTO>> getAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<AttendantDTO> list = attendantService.getAllAttendants(pageable);
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
