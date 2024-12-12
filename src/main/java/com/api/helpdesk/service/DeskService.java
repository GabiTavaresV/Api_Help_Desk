package com.api.helpdesk.service;


import com.api.helpdesk.entity.Attendant;
import com.api.helpdesk.entity.Desk;
import com.api.helpdesk.repository.DeskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeskService {

    @Autowired
    private DeskRepository deskRepository;

    @Autowired
    private AttendantService attendantService;

    public Desk register(Desk desk) {
        if (desk.getAttendant() != null && desk.getAttendant().getId() != null) {
            Attendant attendant = attendantService.getAttendantById(desk.getAttendant().getId());
            desk.setAttendant(attendant);
        }
        return deskRepository.save(desk);
    }


    public List<Desk> getAllDesks() {
        return deskRepository.findAll();
    }

    public Desk getDeskById(Long id) {
        return deskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Desk not found"));
    }
}
