package com.api.helpdesk.service;

import com.api.helpdesk.controller.handler.EmailAlreadyExistsException;
import com.api.helpdesk.dto.AttendantDTO;
import com.api.helpdesk.entity.Attendant;
import com.api.helpdesk.exception.NotFoundDBException;
import com.api.helpdesk.mapper.AttendantMapper;
import com.api.helpdesk.repository.AttendantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AttendantService  {

    @Autowired
    private AttendantRepository attendantRepository;

    @Autowired
    private  AttendantMapper attendantMapper;


    public AttendantDTO register(AttendantDTO attendantDTO) {
        if (attendantRepository.existsByName(attendantDTO.getName())) {
            throw new EmailAlreadyExistsException("Atendente já cadastrado.");
        }
        Attendant attendant = attendantMapper.toEntity(attendantDTO);
        Attendant savedAttendant = attendantRepository.save(attendant);
        return attendantMapper.toDTO(savedAttendant);
    }

    public Page<AttendantDTO> getAllAttendants(Pageable pageable) {
        Page<Attendant> attendants = attendantRepository.findAllActiveAttendants(pageable);
        return attendants
                .map(attendantMapper::toDTO);
    }

    public AttendantDTO getAttendantById(Long id) throws NotFoundDBException {
        Attendant attendant = attendantRepository.findActiveAttendantById(id)
                .orElseThrow(() -> new NotFoundDBException("Atendente não encontrado!"));
        return attendantMapper.toDTO(attendant);
    }

    public Void deleteAttendantById(Long id) throws NotFoundDBException {
        Optional<Attendant> deviceOptional = attendantRepository.findById(id);
        if (deviceOptional.isEmpty()) {
            throw new NotFoundDBException("Atendente não encontrado!");
        }
        attendantRepository.softDeleteAttendantById(id);
        return null;
    }
}

