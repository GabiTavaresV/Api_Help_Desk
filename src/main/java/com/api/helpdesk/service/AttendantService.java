package com.api.helpdesk.service;

import com.api.helpdesk.entity.Attendant;
import com.api.helpdesk.exception.NotFoundDBException;
import com.api.helpdesk.repository.AttendantRepository;
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

    public Attendant getAttendantById(Long id) throws NotFoundDBException {
        return attendantRepository.findById(id)
                .orElseThrow(() -> new NotFoundDBException("Atendente Não encontrado!"));
    }

}
