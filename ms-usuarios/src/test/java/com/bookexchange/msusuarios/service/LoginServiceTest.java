package com.bookexchange.msusuarios.service;

import com.bookexchange.msusuarios.assembler.UsuarioAssembler;
import com.bookexchange.msusuarios.domain.Usuario;
import com.bookexchange.msusuarios.domain.representation.LoginInput;
import com.bookexchange.msusuarios.domain.representation.LoginOutput;
import com.bookexchange.msusuarios.domain.representation.UsuarioOutput;
import com.bookexchange.msusuarios.exception.InternalServerErrorException;
import com.bookexchange.msusuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class LoginServiceTest {
    private UsuarioRepository usuarioRepository;
    private UsuarioAssembler usuarioAssembler;
    private LoginInput loginInput;
    private LoginService loginService;
    private Usuario usuario;
    private LoginOutput loginOutput;
    private UsuarioOutput usuarioOutput;

    @BeforeEach
    void setUp() throws Exception {
        usuarioRepository = mock(UsuarioRepository.class);
        usuarioAssembler = mock(UsuarioAssembler.class);

        loginService = new LoginService(usuarioRepository, usuarioAssembler);

        loginInput = LoginInput.builder()
                .email("test@email.com")
                .senha("test")
                .build();

        usuario = Usuario.builder()
                .id(1)
                .nome("Nome test")
                .email("test@email.com")
                .senha("test")
                .dataCadastro(LocalDate.of(2023, 10, 2))
                .build();

        loginOutput = LoginOutput.builder()
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .build();

        usuarioOutput = UsuarioOutput.builder()
                .email(usuario.getEmail())
                .id(usuario.getId())
                .nome(usuario.getNome())
                .build();
    }

    @Test
    void logarTest() {
        // Dado
        when(usuarioRepository.findByEmailUsuario(any())).thenReturn(Optional.of(usuario));
        // E
        when(usuarioAssembler.toRepresentationOutput(any())).thenReturn(usuarioOutput);
        // Quando
        ResponseEntity<Object> result = loginService.logar(loginInput);
        // Então
        assertNotNull(result.getBody());
    }

    @Test
    void logar2Test() {
        // Dado
        usuario.setEmail("email2@test");
        // E
        when(usuarioRepository.findByEmailUsuario(any())).thenReturn(Optional.of(usuario));
        // E
        when(usuarioAssembler.toRepresentationOutput(any())).thenReturn(usuarioOutput);
        // Quando
        ResponseEntity<Object> result = loginService.logar(loginInput);
        // Então
        assertNotNull(result.getBody());
    }

    @Test
    void logar3Test() {
        // Dado
        usuario.setEmail("email2@test");
        // E
        when(usuarioRepository.findByEmailUsuario(any())).thenReturn(Optional.empty());
        // E
        when(usuarioAssembler.toRepresentationOutput(any())).thenReturn(usuarioOutput);
        // Quando
        ResponseEntity<Object> result = loginService.logar(loginInput);
        // Então
        assertNotNull(result.getBody());
    }

    @Test
    void logarExceptionTest() {
        // Dado
        when(usuarioRepository.findByEmailUsuario(any())).thenThrow(RuntimeException.class);
        // E
        when(usuarioAssembler.toRepresentationOutput(any())).thenReturn(usuarioOutput);
        // Quando
        InternalServerErrorException result = assertThrows(InternalServerErrorException.class,
                ()-> loginService.logar(loginInput));
        // Então
        assertTrue(result.getMessage().contains("Ocorreu um erro ao realizar o login."));
    }
}