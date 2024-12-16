package com.api.helpdesk.service;

import com.api.helpdesk.dto.AttendantDTO;
import com.api.helpdesk.dto.DeskDTO;
import com.api.helpdesk.entity.Desk;
import com.api.helpdesk.exception.NotFoundDBException;
import com.api.helpdesk.mapper.AttendantMapper;
import com.api.helpdesk.mapper.DeskMapper;
import com.api.helpdesk.repository.DeskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeskService {

    @Autowired
    private DeskRepository deskRepository;

    @Autowired
    private AttendantService attendantService;

    private final DeskMapper deskMapper = new DeskMapper();
    private final AttendantMapper attendantMapper = new AttendantMapper();

    public DeskDTO register(DeskDTO deskDTO) {
        Desk desk = deskMapper.toEntity(deskDTO);

        AttendantDTO attendant = attendantService.getAttendantById(deskDTO.getAttendant().getId());
        desk.setAttendant(attendantMapper.toEntity(attendant));

        Desk savedDesk = deskRepository.save(desk);

        return deskMapper.toDTO(savedDesk);
    }

    public List<DeskDTO> getAllDesks() {
        List<Desk> desks = deskRepository.findAll();
        return desks.stream()
                .map(deskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DeskDTO getDeskById(Long id) throws NotFoundDBException {
        Desk desk = deskRepository.findById(id)
                .orElseThrow(() -> new NotFoundDBException("Balcão não encontrado!"));
        return deskMapper.toDTO(desk);
    }
}
