package com.api.helpdesk.controller;

import com.api.helpdesk.dto.DeskDTO;
import com.api.helpdesk.service.AttendantService;
import com.api.helpdesk.service.DeskService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public DeskDTO create(@RequestBody DeskDTO desk) {
       return deskService.register(desk);
    }

    @GetMapping("/findAll")
    public List<DeskDTO> getAll() {
        return deskService.getAllDesks();
    }

    @GetMapping("/{id}")
    public DeskDTO getById(@PathVariable Long id) {
        return deskService.getDeskById(id);
    }
}
