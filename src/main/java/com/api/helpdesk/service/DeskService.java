package com.api.helpdesk.service;

import com.api.helpdesk.dto.AttendantDTO;
import com.api.helpdesk.dto.DeskDTO;
import com.api.helpdesk.entity.Attendant;
import com.api.helpdesk.entity.Desk;
import com.api.helpdesk.exception.NotFoundDBException;
import com.api.helpdesk.exception.SoftDeleteException;
import com.api.helpdesk.mapper.AttendantMapper;
import com.api.helpdesk.mapper.DeskMapper;
import com.api.helpdesk.repository.AttendantRepository;
import com.api.helpdesk.repository.DeskRepository;
import com.api.helpdesk.repository.TicketRepository;
import com.api.helpdesk.utils.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private DeskMapper deskMapper;

    @Autowired
    private AttendantMapper attendantMapper;

    @Autowired
    private AttendantRepository attendantRepository;

    public DeskDTO register(DeskDTO deskDTO) {
        Long deskId = deskDTO.getId();

        if (deskDTO.getIsDeleted() == null) {

            deskDTO.setIsDeleted(false);

        }
        long openTicketsCount = deskRepository.countOpenTicketsByDeskId(deskId, TicketStatus.CONCLUIDO);

        if (openTicketsCount >= 5) {
            throw new IllegalStateException("Não é possível criar mais chamados. O número máximo de chamados em aberto foi atingido.");
        }

        Desk desk = deskMapper.deskDTOToDesk(deskDTO);

        AttendantDTO attendant = attendantService.getAttendantById(deskDTO.getAttendant().getId());
        boolean attendantAssigned = deskRepository.isAttendantAssigned(attendant.getId());

        if (attendantAssigned) {
            throw new IllegalStateException("O atendente já está atribuído a um balcão.");
        }

        Optional<Attendant> deletedAttendant = attendantRepository.findDeletedAttendantById(attendant.getId());

        if (deletedAttendant.isPresent()) {
            throw new SoftDeleteException("Atendente Não encontrado.");
        }
        desk.setAttendant(attendantMapper.attendantDTOToAttendant(attendant));

        Desk savedDesk = deskRepository.save(desk);

        return deskMapper.deskToDeskDTO(savedDesk);
    }


    public Page<DeskDTO> getAllDesks(Pageable pageable) {
        Page<Desk> desksPage = deskRepository.findAllActiveDesks(pageable);

        return desksPage.map(desk -> {
            long openTicketsCount = ticketRepository.countTicketsByDeskIdAndStatusNot(desk.getId(), TicketStatus.CONCLUIDO);
            DeskDTO deskDTO = deskMapper.deskToDeskDTO(desk);
            deskDTO.setOpenTicketsCount((int) openTicketsCount);
            return deskDTO;
        });
    }

    public DeskDTO getDeskById(Long id) throws NotFoundDBException {
        Desk desk = deskRepository.findActiveDeskById(id)
                .orElseThrow(() -> new NotFoundDBException("Balcão não encontrado!"));
        return deskMapper.deskToDeskDTO(desk);
    }

    public Void deleteDeskById(Long id) throws NotFoundDBException {
        Optional<Desk> deskOptional = deskRepository.findById(id);
        if (!deskOptional.isPresent()) {
            throw new NotFoundDBException("Equipamento não encontrado!");
        }

        long activeTicketsCount = ticketRepository.countTicketsByDeskIdAndNotConcluded(id, TicketStatus.CONCLUIDO);

        if (activeTicketsCount > 0) {
            throw new SoftDeleteException("Não é possível deletar o balcão. Existem chamados não concluídos vinculados.");
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
                availableDesks.add(deskMapper.deskToDeskDTO(desk));
                System.out.println("Desk ID: " + desk.getId() + " is added to available desks.");
            }
        }

        return availableDesks;
    }


}
