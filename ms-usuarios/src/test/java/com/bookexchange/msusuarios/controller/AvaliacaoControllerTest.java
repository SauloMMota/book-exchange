package com.bookexchange.msusuarios.controller;

import com.bookexchange.msusuarios.domain.Avaliacao;
import com.bookexchange.msusuarios.service.AvaliacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AvaliacaoControllerTest {

    private AvaliacaoController avaliacaoController;

    private AvaliacaoService avaliacaoService;

    @BeforeEach
    void setUp() {
        avaliacaoService = mock(AvaliacaoService.class);

        avaliacaoController = new AvaliacaoController(avaliacaoService);
    }

    @Test
    void realizarAvaliacao() {
        // Dado
        when(avaliacaoService.realizarAvaliacao(any(), any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());
        // Quando
        ResponseEntity<Object> result = avaliacaoController
                .realizarAvaliacao(1, 2, "Comentario Test");
        // Então
        verify(avaliacaoService, times(1)).realizarAvaliacao(any(), any(), any());
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void buscarAvaliacoes() {
        // Dado
        when(avaliacaoService.buscarAvaliacoes(any(), any(), any())).thenReturn(ResponseEntity.status(HttpStatus.OK).body(Page.empty()));
        // Quando
        ResponseEntity<Page<Avaliacao>> result = avaliacaoController
                .buscarAvaliacoes(1, 2, 2);
        // Então
        verify(avaliacaoService, times(1)).buscarAvaliacoes(any(), any(), any());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}