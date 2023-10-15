package com.bookexchange.msusuarios.controller;

import com.bookexchange.msusuarios.domain.representation.LoginInput;
import com.bookexchange.msusuarios.domain.representation.UsuarioInput;
import com.bookexchange.msusuarios.domain.representation.UsuarioOutput;
import com.bookexchange.msusuarios.service.LoginService;
import com.bookexchange.msusuarios.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest

class LoginControllerTest {

    private LoginController loginController;

    private LoginService loginService;
    private UsuarioService usuarioService;
    private LoginInput loginInput;
    private UsuarioOutput usuarioOutput;
    private UsuarioInput usuarioInput;

    @BeforeEach
    void setUp() {
        loginService = mock(LoginService.class);
        usuarioService = mock(UsuarioService.class);
        loginController = new LoginController(loginService, usuarioService);

        loginInput =  LoginInput.builder()
                .email("email@test.com")
                .senha("senha")
                .build();

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
    void logarTest() {
        // Dado
        when(loginService.logar(any())).thenReturn(ResponseEntity.ok(usuarioOutput));
        // Quando
        ResponseEntity<Object> result = loginController.logar(loginInput);
        // Então
        verify(loginService, times(1)).logar(any());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(usuarioOutput, result.getBody());
    }

    @Test
    void cadastrarTest() {
        // Dado
        when(usuarioService.cadastrar(any()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(usuarioOutput));
        // Quando
        ResponseEntity<UsuarioOutput> result = loginController.cadastrar(usuarioInput);
        // Então
        verify(usuarioService, times(1)).cadastrar(any());
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(usuarioOutput, result.getBody());

    }
}