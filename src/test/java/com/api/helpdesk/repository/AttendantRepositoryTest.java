package com.api.helpdesk.repository;


import com.api.helpdesk.entity.Attendant;
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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AttendantRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AttendantRepository attendantRepository;

    private Attendant attendant;

    @BeforeEach
    public void setUp() {
        attendant = new Attendant();
        attendant.setName("Attendant Name");
        attendant.setDeleted(false);
        entityManager.persistAndFlush(attendant);
    }

    @Test
    public void whenFindAllActiveAttendants_thenReturnActiveAttendants() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Attendant> activeAttendantsPage = attendantRepository.findAllActiveAttendants(pageable);
        assertThat(activeAttendantsPage.hasContent()).isTrue();
        assertTrue(activeAttendantsPage.getContent().contains(attendant));
        assertThat(activeAttendantsPage.getTotalElements()).isGreaterThan(0);
        assertThat(activeAttendantsPage.getSize()).isLessThanOrEqualTo(10);
    }

    @Test
    public void whenFindActiveAttendantById_thenReturnActiveAttendant() {
        Optional<Attendant> foundAttendant = attendantRepository.findActiveAttendantById(attendant.getId());
        assertTrue(foundAttendant.isPresent());
        assertThat(foundAttendant.get().getName()).isEqualTo(attendant.getName());
    }

    @Test
    public void whenSoftDeleteAttendantById_thenAttendantIsDeleted() {
        attendantRepository.softDeleteAttendantById(attendant.getId());
        Optional<Attendant> deletedAttendant = attendantRepository.findActiveAttendantById(attendant.getId());
        assertTrue(deletedAttendant.isEmpty());
    }

    @Test
    public void whenCheckForExistingAttendantByName_thenReturnTrue() {
        boolean exists = attendantRepository.existsByName(attendant.getName());
        assertTrue(exists);
    }

    @Test
    public void whenCheckForNonExistingAttendantByName_thenReturnFalse() {
        boolean exists = attendantRepository.existsByName("nonexistent attendant");
        assertThat(exists).isFalse();
    }

}
