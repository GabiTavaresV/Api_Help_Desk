package com.api.helpdesk.repository;

import com.api.helpdesk.entity.Attendant;
import com.api.helpdesk.entity.Desk;
import com.api.helpdesk.utils.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class DeskRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DeskRepository deskRepository;

    private Desk desk;
    private Attendant attendant;

    @BeforeEach
    public void setUp() {
        desk = new Desk();
        desk.setAttendant(attendant);
        entityManager.persistAndFlush(desk);
    }

    @Test
    public void whenFindAllActiveDesks_thenReturnActiveDesks() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Desk> activeDesksPage = deskRepository.findAllActiveDesks(pageable);
        assertThat(activeDesksPage.hasContent()).isTrue();
        assertTrue(activeDesksPage.getContent().contains(desk));
        assertThat(activeDesksPage.getTotalElements()).isGreaterThan(0);
        assertThat(activeDesksPage.getSize()).isLessThanOrEqualTo(10);
    }

    @Test
    public void whenFindActiveDeskById_thenReturnActiveDesk() {
        Optional<Desk> foundDesk = deskRepository.findActiveDeskById(desk.getId());
        assertTrue(foundDesk.isPresent());
        assertThat(foundDesk.get().getAttendant()).isEqualTo(desk.getAttendant());
    }

    @Test
    public void whenSoftDeleteDeskById_thenDeskIsDeleted() {
        deskRepository.softDeleteDeskById(desk.getId());
        Optional<Desk> deletedDesk = deskRepository.findActiveDeskById(desk.getId());
        assertTrue(deletedDesk.isEmpty());
    }

    @Test
    public void whenCountOpenTicketsByDeskId_thenReturnCountOfOpenTickets() {
        long count = deskRepository.countOpenTicketsByDeskId(desk.getId(), TicketStatus.ABERTO);
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void whenFindAllWithAttendant_thenReturnDesksWithAttendant() {
        List<Desk> desksWithAttendant = deskRepository.findAllWithAttendant();
        assertThat(desksWithAttendant).isEmpty();
    }
}
