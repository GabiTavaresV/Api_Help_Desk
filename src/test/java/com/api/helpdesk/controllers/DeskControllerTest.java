package com.api.helpdesk.controllers;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.api.helpdesk.controller.DeskController;
import com.api.helpdesk.dto.AttendantDTO;
import com.api.helpdesk.dto.DeskDTO;
import com.api.helpdesk.service.DeskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

public class DeskControllerTest {

    private DeskController deskController;
    private DeskService deskService;

    @BeforeEach
    void setUp() {
        deskService = mock(DeskService.class);
        deskController = new DeskController(deskService);
    }

    @Test
    void deveCriarDesk() {
        AttendantDTO attendantDTO = new AttendantDTO();
        DeskDTO desk = new DeskDTO();
        desk.setAttendant(attendantDTO);

        when(deskService.register(any(DeskDTO.class)))
                .thenReturn(desk);

        ResponseEntity<DeskDTO> response = deskController.create(desk);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAttendant()).isEqualTo(attendantDTO);

        verify(deskService).register(any(DeskDTO.class));
    }

    @Test
    void deveRetornarListaDeDesks() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        AttendantDTO attendantDTO = new AttendantDTO();
        DeskDTO desk1 = new DeskDTO();
        desk1.setAttendant(attendantDTO);
        DeskDTO desk2 = new DeskDTO();
        AttendantDTO attendantDTO2 = new AttendantDTO();
        desk2.setAttendant(attendantDTO2);

        Page<DeskDTO> page = new PageImpl<>(Arrays.asList(desk1, desk2), pageable, 2);

        when(deskService.getAllDesks(pageable)).thenReturn(page);

        ResponseEntity<Page<DeskDTO>> response = deskController.getAll(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(2);
        assertThat(response.getBody().getContent()).hasSize(2);
        assertThat(response.getBody().getContent().get(0).getAttendant()).isEqualTo(attendantDTO);
        assertThat(response.getBody().getContent().get(1).getAttendant()).isEqualTo(attendantDTO2);

        verify(deskService).getAllDesks(pageable);
    }

    @Test
    void deveDeletarDesk() {
        Long deskId = 1L;

        ResponseEntity<Void> response = deskController.delete(deskId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(deskService).deleteDeskById(deskId);
    }

    @Test
    void deveRetornarDetalhesDaDesk() {
        Long deskId = 1L;
        DeskDTO deskDetails = new DeskDTO();
        AttendantDTO attendantDTO = new AttendantDTO();
        deskDetails.setAttendant(attendantDTO);

        when(deskService.getDeskDetails(deskId)).thenReturn(deskDetails);

        ResponseEntity<DeskDTO> response = deskController.getDeskDetails(deskId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAttendant()).isEqualTo(attendantDTO);

        verify(deskService).getDeskDetails(deskId);
    }
}