package com.bookexchange.msusuarios.service;

import com.bookexchange.msusuarios.domain.Avaliacao;
import com.bookexchange.msusuarios.domain.Usuario;
import com.bookexchange.msusuarios.exception.EntidadeNaoEncontradaException;
import com.bookexchange.msusuarios.exception.InternalServerErrorException;
import com.bookexchange.msusuarios.repository.AvaliacaoRepository;
import com.bookexchange.msusuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AvaliacaoServiceTest {
    private AvaliacaoRepository avaliacaoRepository;
    private UsuarioRepository usuarioRepository;
    private AvaliacaoService avaliacaoService;
    private Avaliacao avaliacao;
    private Page<Avaliacao> avaliacoesPage;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        avaliacaoRepository = mock(AvaliacaoRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        avaliacaoService = new AvaliacaoService(avaliacaoRepository, usuarioRepository);

        avaliacao = Avaliacao.builder()
                .idUsuarioAvaliado(1)
                .idUsuarioAvaliador(1)
                .comentario("comentario test")
                .build();

        avaliacoesPage = Page.empty(Pageable.ofSize(1));

        usuario = Usuario.builder()
                .id(1)
                .nome("Nome test")
                .email("test@email.com")
                .senha("test")
                .dataCadastro(LocalDate.of(2023, 10, 2))
                .build();
    }

    @Test
    void realizarAvaliacaoTest() {
        // Dado
        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuario));
        // E
        when(avaliacaoRepository.save(any())).thenReturn(avaliacao);
        // Quando
        ResponseEntity<Object> result =
                avaliacaoService.realizarAvaliacao(1, 2,"comentario test");
        // Então
        assertNotNull(result);
    }

    @Test
    void realizarAvaliacaoExceptionTest() {
        // Dado
        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuario));
        // E
        when(avaliacaoRepository.save(any())).thenThrow(RuntimeException.class);
        // Quando
        InternalServerErrorException result = assertThrows(InternalServerErrorException.class,
                () -> avaliacaoService.
                        realizarAvaliacao(1, 2,"comentario test"));
        // Então
        assertTrue(result.getMessage().contains("Ocorreu um erro ao fazer um comentário"));
    }

    @Test
    void realizarAvaliacaoException2Test() {
        // Dado
        when(usuarioRepository.findById(any())).thenReturn(Optional.empty());
        // E
        when(avaliacaoRepository.save(any())).thenThrow(RuntimeException.class);
        // Quando
        EntidadeNaoEncontradaException result = assertThrows(EntidadeNaoEncontradaException.class,
                () -> avaliacaoService.
                        realizarAvaliacao(1, 2,"comentario test"));
        // Então
        assertTrue(result.getMessage().contains("Usuário avaliador não encontrado"));
    }

    @Test
    void buscarAvaliacoesTest() {
        // Dado
        when(avaliacaoRepository.findAllByIdUsuarioAvaliado(any(), any())).thenReturn(avaliacoesPage);
        // Quando
        ResponseEntity<Page<Avaliacao>> result = avaliacaoService.buscarAvaliacoes(1, 0, 10);
        // Então
        assertNotNull(result);
    }

    @Test
    void buscarAvaliacoesExceptionTest() {
        // Dado
        when(avaliacaoRepository.findAllByIdUsuarioAvaliado(any(), any())).thenThrow(RuntimeException.class);
        // Quando
        InternalServerErrorException result = assertThrows(InternalServerErrorException.class,
                ()-> avaliacaoService.buscarAvaliacoes(1, 0, 10));
        // Então
        assertTrue(result.getMessage().contains("Ocorreu um erro ao realizar a busca de avalições do usuário"));
    }

}