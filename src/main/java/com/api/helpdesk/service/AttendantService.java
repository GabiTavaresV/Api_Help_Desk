package com.api.helpdesk.service;

import com.api.helpdesk.controller.handler.EmailAlreadyExistsException;
import com.api.helpdesk.dto.AttendantDTO;
import com.api.helpdesk.entity.Attendant;
import com.api.helpdesk.entity.Users;
import com.api.helpdesk.exception.NotFoundDBException;
import com.api.helpdesk.mapper.AttendantMapper;
import com.api.helpdesk.mapper.UserMapper;
import com.api.helpdesk.repository.AttendantRepository;
import com.api.helpdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendantService {

    @Autowired
    private AttendantRepository attendantRepository;

    private final AttendantMapper attendantMapper = new AttendantMapper();

    @Autowired
    public AttendantService(AttendantRepository attendantRepository) {
        this.attendantRepository = attendantRepository;
    }

    public AttendantDTO register(AttendantDTO attendantDTO) {
        if (attendantRepository.existsByName(attendantDTO.getName())) {
            throw new EmailAlreadyExistsException("Atendente já cadastrado.");
        }
        Attendant attendant = attendantMapper.toEntity(attendantDTO);
        Attendant savedAttendant = attendantRepository.save(attendant);
        return attendantMapper.toDTO(savedAttendant);
    }

    public List<AttendantDTO> getAllAttendants() {
        List<Attendant> attendants = attendantRepository.findAllActiveAttendants();
        return attendants.stream()
                .map(attendantMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AttendantDTO getAttendantById(Long id) throws NotFoundDBException {
        Attendant attendant = attendantRepository.findActiveAttendantById(id)
                .orElseThrow(() -> new NotFoundDBException("Atendente não encontrado!"));
        return attendantMapper.toDTO(attendant);
    }

    public Void deleteAttendantById(Long id) throws NotFoundDBException {
        Optional<Attendant> deviceOptional = attendantRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            throw new NotFoundDBException("Atendente não encontrado!");
        }
        attendantRepository.softDeleteAttendantById(id);
        return null;
    }
}

