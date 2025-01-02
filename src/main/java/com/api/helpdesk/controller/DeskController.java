package com.api.helpdesk.controller;

import com.api.helpdesk.dto.DeskDTO;
import com.api.helpdesk.service.DeskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor()
@RestController
@RequestMapping(value = "/desk")
public class DeskController {

    private DeskService deskService;

    @PostMapping
    public ResponseEntity<DeskDTO> create(@RequestBody DeskDTO desk) {
        DeskDTO createDesk = deskService.register(desk);
        return new ResponseEntity<>(createDesk, HttpStatus.CREATED);
    }

    @GetMapping("/findAll")
    public ResponseEntity<Page<DeskDTO>> getAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<DeskDTO> list = deskService.getAllDesks(pageable);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        deskService.deleteDeskById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<DeskDTO> getDeskDetails(@PathVariable("id") Long id) {
        DeskDTO deskDetails = deskService.getDeskDetails(id);
        return new ResponseEntity<>(deskDetails, HttpStatus.OK);
    }
}
