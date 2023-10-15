package com.bookexchange.msusuarios.controller;

import com.bookexchange.msusuarios.domain.representation.UsuarioInput;
import com.bookexchange.msusuarios.domain.representation.UsuarioOutput;
import com.bookexchange.msusuarios.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UsuarioControllerTest {
    private UsuarioService usuarioService;
    private UsuarioController usuarioController;
    private UsuarioOutput usuarioOutput;
    private UsuarioInput usuarioInput;

    @BeforeEach
    void setUp() {
        usuarioService = mock(UsuarioService.class);

        usuarioController = new UsuarioController(usuarioService);

        usuarioOutput = UsuarioOutput.builder()
                .email("email@test.com")
                .id(1)
                .nome("Nome Test")
                .build();

        usuarioInput = UsuarioInput.builder()
                .nome("Nome Test")
                .email("test@email.com")
                .senha("1234")
                .build();
    }

    @Test
    void getUsuarioPorIdTest() {
        // Dado
        when(usuarioService.getUsuarioPorId(any())).thenReturn(ResponseEntity.ok(usuarioOutput));
        // Quando
        ResponseEntity<UsuarioOutput> result = usuarioController.getUsuarioPorId(1);
        // Então
        verify(usuarioService, times(1)).getUsuarioPorId(any());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(usuarioOutput, result.getBody());
    }

    @Test
    void alterarTest() {
        // Dado
        when(usuarioService.alterar(any(), any())).thenReturn(ResponseEntity.ok(usuarioOutput));
        // Quando
        ResponseEntity<UsuarioOutput> result = usuarioController.alterar(1, usuarioInput);
        // Então
        verify(usuarioService, times(1)).alterar(any(), any());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(usuarioOutput, result.getBody());
    }

    @Test
    void statusTest() {
        // Quando
        String result = usuarioController.status();
        // Então
        assertEquals("OK", result);
    }
}