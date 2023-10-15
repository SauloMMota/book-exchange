package com.bookexchange.msusuarios.service;

import com.bookexchange.msusuarios.assembler.UsuarioAssembler;
import com.bookexchange.msusuarios.domain.Usuario;
import com.bookexchange.msusuarios.domain.representation.LoginInput;
import com.bookexchange.msusuarios.domain.representation.UsuarioInput;
import com.bookexchange.msusuarios.domain.representation.UsuarioOutput;
import com.bookexchange.msusuarios.exception.InternalServerErrorException;
import com.bookexchange.msusuarios.exception.NegocioException;
import com.bookexchange.msusuarios.repository.UsuarioRepository;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class UsuarioServiceTest {
    private UsuarioRepository usuarioRepository;
    private UsuarioAssembler usuarioAssembler;
    private UsuarioService usuarioService;
    private Usuario usuario;
    private UsuarioInput usuarioInput;
    private UsuarioOutput usuarioOutput;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        usuarioAssembler = mock(UsuarioAssembler.class);

        usuarioService = new UsuarioService(usuarioRepository, usuarioAssembler);

        usuarioInput = UsuarioInput.builder()
                .nome("Nome Test")
                .email("test@email.com")
                .senha("1234")
                .build();

        usuario = Usuario.builder()
                .id(1)
                .nome("Nome test")
                .email("test@email.com")
                .senha("test")
                .dataCadastro(LocalDate.of(2023, 10, 2))
                .build();

        usuarioOutput = UsuarioOutput.builder()
                .email(usuario.getEmail())
                .id(usuario.getId())
                .nome(usuario.getNome())
                .build();
    }

    @Test
    void cadastrarTest() {
        // Dado
        when(usuarioRepository.findByEmail(any())).thenReturn(StringUtils.EMPTY);
        // E
        when(usuarioAssembler.toModel(any())).thenReturn(usuario);
        // E
        when(usuarioRepository.save(any())).thenReturn(usuario);
        // E
        when(usuarioAssembler.toRepresentationOutput(any())).thenReturn(usuarioOutput);
        // Quando
        ResponseEntity<UsuarioOutput> result = usuarioService.cadastrar(usuarioInput);
        // Então
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void cadastrarNegocioExceptionTest() {
        // Dado
        when(usuarioRepository.findByEmail(any())).thenReturn("test@email.com");
        // Quando
        NegocioException result = assertThrows(NegocioException.class,
                () ->  usuarioService.cadastrar(usuarioInput));
        // Então
        assertTrue(result.getMessage().contains("O email informado já está em uso"));
    }

    @Test
    void cadastrarExceptionTest() {
        // Dado
        when(usuarioRepository.findByEmail(any())).thenThrow(RuntimeException.class);
        // Quando
        InternalServerErrorException result = assertThrows(InternalServerErrorException.class,
                () ->  usuarioService.cadastrar(usuarioInput));
        // Então
        assertTrue(result.getMessage().contains("Ocorreu um erro ao cadastrar o usuário"));
    }

    @Test
    void getUsuarioPorIdTest() {
        // Dado
        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuario));
        // E
        when(usuarioAssembler.toRepresentationOutput(any())).thenReturn(usuarioOutput);
        // Quando
        ResponseEntity<UsuarioOutput> result = usuarioService.getUsuarioPorId(1);
        //Então
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void getUsuarioPorIdNegocioException() {
        // Dado
        when(usuarioRepository.findById(any())).thenReturn(Optional.empty());
        // Quando
        NegocioException result = assertThrows(NegocioException.class,
                ()-> usuarioService.getUsuarioPorId(1));
        //Então
        assertTrue(result.getMessage().contains("Usuário não encontrado"));
    }

    @Test
    void alterarTest() {
        // Dado
        when(usuarioRepository.findByEmail(any())).thenReturn(StringUtils.EMPTY);
        // E
        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuario));
        // E
        when(usuarioRepository.save(any())).thenReturn(usuario);
        // E
        when(usuarioAssembler.toRepresentationOutput(any())).thenReturn(usuarioOutput);
        // Quando
        ResponseEntity<UsuarioOutput> result = usuarioService.alterar(1, usuarioInput);
        // Então
        assertNotNull(result);
    }

    @Test
    void alterarNegocioExceptionTest() {
        // Dado
        Usuario usuario2 = Usuario.builder()
                .id(23)
                .email("test@email.com")
                .build();
        // E
        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuario));
        //E
        when(usuarioRepository.findByEmailUsuario(any())).thenReturn(Optional.of(usuario2));
        // Quando
        NegocioException result = assertThrows(NegocioException.class,
                () -> usuarioService.alterar(1, usuarioInput));
        // Então
        assertTrue(result.getMessage().contains("O email informado já está em uso"));
    }

    @Test
    void alterarExceptionTest() {
        // Dado
        when(usuarioRepository.findById(any())).thenThrow(RuntimeException.class);
        // Quando
        InternalServerErrorException result = assertThrows(InternalServerErrorException.class,
                () -> usuarioService.alterar(1, usuarioInput));
        // Então
        assertTrue(result.getMessage().contains("Ocorreu um erro ao alterar os dados do usuário"));
    }

}