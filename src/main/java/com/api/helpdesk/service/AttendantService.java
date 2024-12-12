package com.api.helpdesk.service;

import com.api.helpdesk.entity.Attendant;
import com.api.helpdesk.repository.AttendantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendantService {

    @Autowired
    private AttendantRepository attendantRepository;

    public Attendant register(Attendant attendant) {
        return attendantRepository.save(attendant);
    }

    public List<Attendant> getAllAttendants() {
        return attendantRepository.findAll();
    }

    public Attendant getAttendantById(Long id) {
        return attendantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Attendant not found"));
    }
}
