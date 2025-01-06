package com.api.helpdesk.service;

import com.api.helpdesk.dto.AttendantDTO;
import com.api.helpdesk.dto.DeskDTO;
import com.api.helpdesk.entity.Attendant;
import com.api.helpdesk.entity.Desk;
import com.api.helpdesk.exception.NotFoundDBException;
import com.api.helpdesk.mapper.AttendantMapper;
import com.api.helpdesk.mapper.DeskMapper;
import com.api.helpdesk.repository.AttendantRepository;
import com.api.helpdesk.repository.DeskRepository;
import com.api.helpdesk.repository.TicketRepository;
import com.api.helpdesk.utils.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeskServiceTest {

    @Mock
    private DeskRepository deskRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private AttendantService attendantService;

    @Mock
    private DeskMapper deskMapper;

    @Mock
    private AttendantMapper attendantMapper;

    @Mock
    private AttendantRepository attendantRepository;

    @InjectMocks
    private DeskService deskService;

    private DeskDTO deskDTO;
    private Desk desk;
    private AttendantDTO attendantDTO;

    @BeforeEach
    void setUp() {
        deskDTO = new DeskDTO();
        desk = new Desk();
        attendantDTO = new AttendantDTO();
    }

    @Test
    void testRegister() {
        deskDTO.setId(1L);
        deskDTO.setAttendant(attendantDTO);
        attendantDTO.setId(1L);

        when(deskRepository.countOpenTicketsByDeskId(deskDTO.getId(), TicketStatus.CONCLUIDO)).thenReturn(0L);
        when(deskMapper.deskDTOToDesk(deskDTO)).thenReturn(desk);
        when(attendantService.getAttendantById(attendantDTO.getId())).thenReturn(attendantDTO);
        when(attendantMapper.attendantDTOToAttendant(attendantDTO)).thenReturn(new Attendant());

        doReturn(desk).when(deskRepository).save(any(Desk.class));

        when(deskRepository.isAttendantAssigned(attendantDTO.getId())).thenReturn(false);

        when(attendantRepository.findDeletedAttendantById(attendantDTO.getId())).thenReturn(Optional.empty());

        when(deskMapper.deskToDeskDTO(desk)).thenReturn(deskDTO);

        DeskDTO result = deskService.register(deskDTO);

        verify(deskRepository).save(any(Desk.class));
        assertThat(result).isEqualTo(deskDTO);
    }

    @Test
    void testRegister_MaxTicketsReached() {
        deskDTO.setId(1L);
        when(deskRepository.countOpenTicketsByDeskId(deskDTO.getId(), TicketStatus.CONCLUIDO)).thenReturn(5L);

        assertThrows(IllegalStateException.class, () -> deskService.register(deskDTO));
    }

    @Test
    void testGetAllDesks() {
        Pageable pageable = Pageable.ofSize(10);
        Page<Desk> desksPage = new PageImpl<>(List.of(desk));
        when(deskRepository.findAllActiveDesks(pageable)).thenReturn(desksPage);
        when(deskMapper.deskToDeskDTO(desk)).thenReturn(deskDTO);
        when(ticketRepository.countTicketsByDeskIdAndStatusNot(desk.getId(), TicketStatus.CONCLUIDO)).thenReturn(2L);

        Page<DeskDTO> result = deskService.getAllDesks(pageable);

        assertThat(result.getContent()).containsExactly(deskDTO);
        assertThat(deskDTO.getOpenTicketsCount()).isEqualTo(2);
    }

    @Test
    void testGetDeskById() throws NotFoundDBException {
        Long id = 1L;
        when(deskRepository.findActiveDeskById(id)).thenReturn(Optional.of(desk));
        when(deskMapper.deskToDeskDTO(desk)).thenReturn(deskDTO);

        DeskDTO result = deskService.getDeskById(id);

        assertThat(result).isEqualTo(deskDTO);
    }

    @Test
    void testGetDeskById_NotFound() {
        Long id = 1L;
        when(deskRepository.findActiveDeskById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundDBException.class, () -> deskService.getDeskById(id));
    }

    @Test
    void testDeleteDeskById() throws NotFoundDBException {
        Long id = 1L;
        when(deskRepository.findById(id)).thenReturn(Optional.of(desk));

        deskService.deleteDeskById(id);

        verify(deskRepository).softDeleteDeskById(id);
    }

    @Test
    void testDeleteDeskById_NotFound() {
        Long id = 1L;
        when(deskRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundDBException.class, () -> deskService.deleteDeskById(id));
    }

    @Test
    void testGetOpenTicketsCountForDesk() {
        Long deskId = 1L;
        when(ticketRepository.countTicketsByDeskIdAndStatusNot(deskId, TicketStatus.CONCLUIDO)).thenReturn(3L);

        long result = deskService.getOpenTicketsCountForDesk(deskId);

        assertThat(result).isEqualTo(3);
    }

    @Test
    void testGetDeskDetails() throws NotFoundDBException {
        Long deskId = 1L;
        deskDTO.setId(deskId);
        when(deskRepository.findActiveDeskById(deskId)).thenReturn(Optional.of(desk));
        when(ticketRepository.countTicketsByDeskIdAndStatusNot(deskId, TicketStatus.CONCLUIDO)).thenReturn(2L);
        when(deskMapper.deskToDeskDTO(desk)).thenReturn(deskDTO);

        DeskDTO result = deskService.getDeskDetails(deskId);

        assertThat(result).isEqualTo(deskDTO);
        assertThat(result.getOpenTicketsCount()).isEqualTo(2);
    }

    @Test
    void testFindAvailableDesks() {
        Desk desk1 = new Desk();
        desk1.setId(1L);
        Desk desk2 = new Desk();
        desk2.setId(2L);
        Desk desk3 = new Desk();
        desk3.setId(3L);

        List<Desk> desksWithAttendant = List.of(desk1, desk2, desk3);

        when(deskRepository.findAllWithAttendant()).thenReturn(desksWithAttendant);

        when(ticketRepository.countOpenTicketsByDeskId(desk1.getId(), TicketStatus.ABERTO)).thenReturn(3L);
        when(ticketRepository.countOpenTicketsByDeskId(desk2.getId(), TicketStatus.ABERTO)).thenReturn(5L);
        when(ticketRepository.countOpenTicketsByDeskId(desk3.getId(), TicketStatus.ABERTO)).thenReturn(1L);

        DeskDTO deskDTO1 = new DeskDTO();
        deskDTO1.setId(desk1.getId());
        DeskDTO deskDTO3 = new DeskDTO();
        deskDTO3.setId(desk3.getId());

        when(deskMapper.deskToDeskDTO(desk1)).thenReturn(deskDTO1);
        when(deskMapper.deskToDeskDTO(desk3)).thenReturn(deskDTO3);

        List<DeskDTO> availableDesks = deskService.findAvailableDesks();

        assertThat(availableDesks).hasSize(2);
        assertThat(availableDesks).contains(deskDTO1, deskDTO3);
    }
}