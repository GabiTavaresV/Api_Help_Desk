package com.api.helpdesk.controllers;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.api.helpdesk.controller.AttendantController;
import com.api.helpdesk.dto.AttendantDTO;
import com.api.helpdesk.service.AttendantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

public class AttendantControllerTest {

    private AttendantController attendantController;
    private AttendantService attendantService;

    @BeforeEach
    void setUp() {
        attendantService = mock(AttendantService.class);
        attendantController = new AttendantController(attendantService);
    }

    @Test
    void whenPostAttendant_thenCreateAttendant() {
        AttendantDTO attendant = new AttendantDTO();
        attendant.setName("Maria Aparecida");

        when(attendantService.register(any(AttendantDTO.class)))
                .thenReturn(attendant);

        ResponseEntity<AttendantDTO> response = attendantController.create(attendant);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Maria Aparecida");

        verify(attendantService).register(any(AttendantDTO.class));
    }

    @Test
    void whenGetAttendants_thenListAllAttendants() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        AttendantDTO attendant1 = new AttendantDTO();
        attendant1.setName("John Doe");
        AttendantDTO attendant2 = new AttendantDTO();
        attendant2.setName("Jane Doe");

        Page<AttendantDTO> page = new PageImpl<>(Arrays.asList(attendant1, attendant2), pageable, 2);

        when(attendantService.getAllAttendants(pageable)).thenReturn(page);

        ResponseEntity<Page<AttendantDTO>> response = attendantController.getAll(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(2);
        assertThat(response.getBody().getContent()).hasSize(2);
        assertThat(response.getBody().getContent().get(0).getName()).isEqualTo("John Doe");
        assertThat(response.getBody().getContent().get(1).getName()).isEqualTo("Jane Doe");

        verify(attendantService).getAllAttendants(pageable);
    }

    @Test
    void whenGetAttendantById_thenReturnAttendant() {
        Long attendantId = 1L;
        AttendantDTO attendant = new AttendantDTO();
        attendant.setName("John Doe");

        when(attendantService.getAttendantById(attendantId)).thenReturn(attendant);

        ResponseEntity<AttendantDTO> response = attendantController.getById(attendantId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("John Doe");

        verify(attendantService).getAttendantById(attendantId);
    }

    @Test
    void whenDeleteAttendantById_thenAttendantIsDeleted() {
        Long attendantId = 1L;

        ResponseEntity<Void> response = attendantController.delete(attendantId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(attendantService).deleteAttendantById(attendantId);
    }
}