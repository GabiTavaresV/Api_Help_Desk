package com.api.helpdesk.controller;

import com.api.helpdesk.dto.DeskDTO;
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

    @PostMapping
    public ResponseEntity<DeskDTO> create(@RequestBody DeskDTO desk) {
        DeskDTO createDesk = deskService.register(desk);
        return new ResponseEntity<>(createDesk, HttpStatus.CREATED);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<DeskDTO>> getAll() {
        List<DeskDTO> list = deskService.getAllDesks();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeskDTO> getById(@PathVariable Long id) {
        DeskDTO desk = deskService.getDeskById(id);
        return new ResponseEntity<>(desk, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deskService.deleteDeskById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<DeskDTO> getDeskDetails(@PathVariable Long id) {
        DeskDTO deskDetails = deskService.getDeskDetails(id);
        return new ResponseEntity<>(deskDetails, HttpStatus.OK);
    }
}
