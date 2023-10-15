package com.bookexchange.mslivros.service;

import com.bookexchange.mslivros.domain.Colecao;
import com.bookexchange.mslivros.domain.Informacao;
import com.bookexchange.mslivros.domain.Livro;
import com.bookexchange.mslivros.exception.EntidadeNaoEncontradaException;
import com.bookexchange.mslivros.exception.InternalServerErrorException;
import com.bookexchange.mslivros.exception.NegocioException;
import com.bookexchange.mslivros.repository.ColecaoRepository;
import com.bookexchange.mslivros.repository.InformacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class InformacaoServiceTest {

    private final static Integer ID_COLECAO = 1;
    private final static Integer ID_USUARIO = 1;
    private final static Integer ID_LIVRO = 1;
    private final static String INFORMACAO = "Test";

    private InformacaoRepository informacaoRepository;
    private ColecaoRepository colecaoRepository;
    private InformacaoService informacaoService;
    private Colecao colecao;
    private Livro livro;
    private Set<Livro> livros;
    private Set<Integer> idsLivros;
    private Informacao informacao;

    @BeforeEach
    void setUp() {
        informacaoRepository = mock(InformacaoRepository.class);
        colecaoRepository = mock(ColecaoRepository.class);
        informacaoService = new InformacaoService(informacaoRepository, colecaoRepository);

        livro = Livro.builder()
                .id(1)
                .autor("Test")
                .titulo("Test")
                .genero("Test")
                .descricao("Test")
                .build();
        livros = new HashSet<>();
        livros.add(livro);

        colecao = Colecao.builder()
                .idUsuario(1)
                .id(1)
                .livros(livros)
                .build();

        informacao = Informacao.builder()
                .idColecao(ID_COLECAO)
                .idLivro(1)
                .informacao("Test")
                .build();

        idsLivros = Set.of(1, 2, 3);
    }

    @Test
    void incluirInformacaoTest() {
        // Dado
        when(colecaoRepository.findByIdUsuario(any())).thenReturn(colecao);
        // E
        when(informacaoRepository.save(any())).thenReturn(informacao);
        // Quando
        ResponseEntity<Object> result =
                informacaoService.incluirInformacao(ID_USUARIO, ID_COLECAO, INFORMACAO);
        // Então
        assertNotNull(result);
    }
    @Test
    void incluirInformacao_EntidadeNaoEncontradaExceptionTest() {
        // Dado
        when(colecaoRepository.findByIdUsuario(any())).thenReturn(null);
        // E
        when(informacaoRepository.save(any())).thenReturn(informacao);
        // Quando
        EntidadeNaoEncontradaException result =
                assertThrows(EntidadeNaoEncontradaException.class,
                        () -> informacaoService.incluirInformacao(ID_USUARIO, ID_COLECAO, INFORMACAO));
        // Então
        assertTrue(result.getMessage().contains("Não existe coleção ou não" +
                " tem livros na coleção para serem removidos"));
    }

    @Test
    void incluirInformacao_EntidadeNaoEncontradaException2Test() {
        // Dado
        colecao.setLivros(Collections.emptySet());
        //E
        when(colecaoRepository.findByIdUsuario(any())).thenReturn(colecao);
        // E
        when(informacaoRepository.save(any())).thenReturn(informacao);
        // Quando
        EntidadeNaoEncontradaException result =
                assertThrows(EntidadeNaoEncontradaException.class,
                        () -> informacaoService.incluirInformacao(ID_USUARIO, ID_COLECAO, INFORMACAO));
        // Então
        assertTrue(result.getMessage().contains("Não existe coleção ou não" +
                " tem livros na coleção para serem removidos"));
    }

    @Test
    void incluirInformacao_ExceptionTest() {
        // Dado
        colecao.setLivros(Collections.emptySet());
        //E
        when(colecaoRepository.findByIdUsuario(any())).thenThrow(RuntimeException.class);
        // Quando
        InternalServerErrorException result =
                assertThrows(InternalServerErrorException.class,
                        () -> informacaoService.incluirInformacao(ID_USUARIO, ID_COLECAO, INFORMACAO));
        // Então
        assertTrue(result.getMessage().contains("Ocorreu um erro ao adicionar informações"));
    }

    @Test
    void removerInformacaoTest() {
        // Dado
        when(informacaoRepository.findByIdColecaoAndIdLivro(any(), any())).thenReturn(informacao);
        // E
        doNothing().when(informacaoRepository).deleteAll();
        // Quando
        informacaoService.removerInformacao(ID_COLECAO, idsLivros);
        // Então
        verify(informacaoRepository, times(3))
                .findByIdColecaoAndIdLivro(any(), any());
    }

    @Test
    void buscarColecaoPorColecaoElivroTest() {
        // Dado
        when(informacaoRepository.findByIdColecaoAndIdLivro(any(), any())).thenReturn(informacao);
        // Quando
        Informacao informacao = informacaoService.buscarColecaoPorColecaoElivro(ID_COLECAO, ID_LIVRO);
        // Então
        assertNotNull(informacao);
    }

}