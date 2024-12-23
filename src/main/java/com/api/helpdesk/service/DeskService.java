package com.api.helpdesk.service;

import com.api.helpdesk.dto.AttendantDTO;
import com.api.helpdesk.dto.DeskDTO;
import com.api.helpdesk.entity.Desk;
import com.api.helpdesk.exception.NotFoundDBException;
import com.api.helpdesk.mapper.AttendantMapper;
import com.api.helpdesk.mapper.DeskMapper;
import com.api.helpdesk.repository.DeskRepository;
import com.api.helpdesk.repository.TicketRepository;
import com.api.helpdesk.utils.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeskService {

    @Autowired
    private DeskRepository deskRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private AttendantService attendantService;

    private final DeskMapper deskMapper = new DeskMapper();
    private final AttendantMapper attendantMapper = new AttendantMapper();

    public DeskDTO register(DeskDTO deskDTO) {
        Long deskId = deskDTO.getId();

        long openTicketsCount = deskRepository.countOpenTicketsByDeskId(deskId, TicketStatus.CONCLUIDO);

        if (openTicketsCount >= 5) {
            throw new IllegalStateException("Não é possível criar mais chamados. O número máximo de chamados em aberto foi atingido.");
        }

        Desk desk = deskMapper.toEntity(deskDTO);

        AttendantDTO attendant = attendantService.getAttendantById(deskDTO.getAttendant().getId());
        desk.setAttendant(attendantMapper.toEntity(attendant));

        Desk savedDesk = deskRepository.save(desk);

        return deskMapper.toDTO(savedDesk);
    }

    public List<DeskDTO> getAllDesks() {
        List<Desk> desks = deskRepository.findAllActiveDesks();
        List<DeskDTO> deskDTOs = new ArrayList<>();

        for (Desk desk : desks) {
            long openTicketsCount = ticketRepository.countTicketsByDeskIdAndStatusNot(desk.getId(), TicketStatus.CONCLUIDO);
            DeskDTO deskDTO = deskMapper.toDTO(desk);
            deskDTO.setOpenTicketsCount((int) openTicketsCount);
            deskDTOs.add(deskDTO);
        }

        return deskDTOs;
    }

    public DeskDTO getDeskById(Long id) throws NotFoundDBException {
        Desk desk = deskRepository.findActiveDeskById(id)
                .orElseThrow(() -> new NotFoundDBException("Balcão não encontrado!"));
        return deskMapper.toDTO(desk);
    }

    public Void deleteDeskById(Long id) throws NotFoundDBException {
        Optional<Desk> deskOptional = deskRepository.findById(id);
        if (!deskOptional.isPresent()) {
            throw new NotFoundDBException("Equipamento não encontrado!");
        }
        deskRepository.softDeleteDeskById(id);
        return null;
    }

    public long getOpenTicketsCountForDesk(Long deskId) {
        return ticketRepository.countTicketsByDeskIdAndStatusNot(deskId, TicketStatus.CONCLUIDO);
    }

    public DeskDTO getDeskDetails(Long deskId) throws NotFoundDBException {
        DeskDTO desk = getDeskById(deskId);
        Long openTicketsCount = getOpenTicketsCountForDesk(deskId);
        desk.setOpenTicketsCount(openTicketsCount.intValue());
        return desk;
    }

    public List<DeskDTO> findAvailableDesks() {
        List<Desk> desksWithAttendant = deskRepository.findAllWithAttendant();
        List<DeskDTO> availableDesks = new ArrayList<>();

        for (Desk desk : desksWithAttendant) {
            long openTicketsCount = ticketRepository.countOpenTicketsByDeskId(desk.getId(), TicketStatus.ABERTO);

            System.out.println("Desk ID: " + desk.getId() + ", Open Tickets: " + openTicketsCount);

            if (openTicketsCount < 5) {
                availableDesks.add(deskMapper.toDTO(desk));
                System.out.println("Desk ID: " + desk.getId() + " is added to available desks.");
            }
        }

        return availableDesks;
    }


}
