package com.bookexchange.mslivros.service;

import com.bookexchange.mslivros.domain.Colecao;
import com.bookexchange.mslivros.domain.Informacao;
import com.bookexchange.mslivros.domain.Livro;
import com.bookexchange.mslivros.domain.representation.LivroPage;
import com.bookexchange.mslivros.exception.InternalServerErrorException;
import com.bookexchange.mslivros.exception.NegocioException;
import com.bookexchange.mslivros.repository.ColecaoRepository;
import com.bookexchange.mslivros.repository.InformacaoRepository;
import com.bookexchange.mslivros.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ColecaoServiceTest {

    private final static Integer ID_USUARIO = 1;

    private ColecaoRepository colecaoRepository;
    private LivroRepository livroRepository;
    private InformacaoService informacaoService;
    private ColecaoService colecaoService;
    private List<Livro> livros;
    private Set<Livro> livroSet;
    private Livro livro;
    private Colecao colecao;
    private List<Colecao> colecoes;
    private Set<Integer> idsLivros;
    private Informacao informacao;

    @BeforeEach
    void setUp() {
        colecaoRepository = mock(ColecaoRepository.class);
        livroRepository = mock(LivroRepository.class);
        informacaoService = mock(InformacaoService.class);

        colecaoService = new ColecaoService(colecaoRepository,
                livroRepository, informacaoService);

        livro = Livro.builder()
                .id(1)
                .autor("Test")
                .titulo("Test")
                .genero("Test")
                .descricao("Test")
                .build();

        livros = new ArrayList<>();
        livros.add(livro);

        livroSet = new HashSet<>();
        livroSet.add(livro);

        colecao = Colecao.builder()
                .idUsuario(1)
                .id(1)
                .livros(livroSet)
                .build();

        idsLivros = Set.of(1, 2, 3);

        colecoes = new ArrayList<>();
        colecoes.add(colecao);

        informacao = Informacao.builder()
                .idColecao(1)
                .idLivro(1)
                .informacao("Test")
                .build();
    }

    @Test
    void incluirLivroColecaoTest() {
        // Dado
        when(livroRepository.findAllById(any())).thenReturn(livros);
        // E
        when(colecaoRepository.findByIdUsuario(any())).thenReturn(null);
        // E
        when(colecaoRepository.save(any())).thenReturn(colecao);
        // Quando
        ResponseEntity<Object> result = colecaoService.incluirLivroColecao(idsLivros, ID_USUARIO);
        // Então
        assertNotNull(result);
    }

    @Test
    void incluirLivroColecao2Test() {
        // Dado
        when(livroRepository.findAllById(any())).thenReturn(livros);
        // E
        when(colecaoRepository.findByIdUsuario(any())).thenReturn(colecao);
        // E
        when(colecaoRepository.save(any())).thenReturn(colecao);
        // Quando
        ResponseEntity<Object> result = colecaoService.incluirLivroColecao(idsLivros, ID_USUARIO);
        // Então
        assertNotNull(result);
    }

    @Test
    void incluirLivroColecaoExceptionTest() {
        // Dado
        when(livroRepository.findAllById(any())).thenThrow(RuntimeException.class);
        // Quando
        InternalServerErrorException result = assertThrows(InternalServerErrorException.class,
                ()-> colecaoService.incluirLivroColecao(idsLivros, ID_USUARIO));
        // Então
        assertTrue(result.getMessage().contains("Ocorreu um erro ao incluir livros na coleção"));
    }

    @Test
    void deleterLivrosColecaoTest() {
        // Dado
        when(colecaoRepository.findByIdUsuario(any())).thenReturn(colecao);
        // E
        when(colecaoRepository.save(any())).thenReturn(colecao);
        // E
        doNothing().when(informacaoService).removerInformacao(1, idsLivros);
        // Quando
        ResponseEntity<Object> result = colecaoService.deleterLivrosColecao(ID_USUARIO, idsLivros);
        // Então
        assertNotNull(result);
    }

    @Test
    void deleterLivrosColecao_NegocioExceptionTest() {
        // Dado
        when(colecaoRepository.findByIdUsuario(any())).thenReturn(colecao);
        // E
        when(colecaoRepository.save(any())).thenReturn(colecao);
        // E
        doNothing().when(informacaoService).removerInformacao(1, idsLivros);
        // Quando
        NegocioException result = assertThrows(NegocioException.class,
                ()-> colecaoService.deleterLivrosColecao(null, idsLivros));
        // Então
        assertTrue(result.getMessage().contains("Não foi informado o código do usuário"));
    }

    @Test
    void deleterLivrosColecao_NegocioException2Test() {
        // Dado
        when(colecaoRepository.findByIdUsuario(any())).thenReturn(null);
        // E
        when(colecaoRepository.save(any())).thenReturn(colecao);
        // E
        doNothing().when(informacaoService).removerInformacao(1, idsLivros);
        // Quando
        NegocioException result = assertThrows(NegocioException.class,
                ()-> colecaoService.deleterLivrosColecao(ID_USUARIO, idsLivros));
        // Então
        assertTrue(result.getMessage().contains("Não existe coleção ou não tem livros" +
                " na coleção para serem removidos"));
    }

    @Test
    void deleterLivrosColecao_NegocioException3Test() {
        // Dado
        colecao.setLivros(Collections.emptySet());
        // E
        when(colecaoRepository.findByIdUsuario(any())).thenReturn(colecao);
        // E
        when(colecaoRepository.save(any())).thenReturn(colecao);
        // E
        doNothing().when(informacaoService).removerInformacao(1, idsLivros);
        // Quando
        NegocioException result = assertThrows(NegocioException.class,
                ()-> colecaoService.deleterLivrosColecao(ID_USUARIO, idsLivros));
        // Então
        assertTrue(result.getMessage().contains("Não existe coleção ou não tem livros" +
                " na coleção para serem removidos"));
    }

    @Test
    void deleterLivrosColecao_ExceptionTest() {
        // Dado
        when(colecaoRepository.findByIdUsuario(any())).thenThrow(RuntimeException.class);
        // Quando
        InternalServerErrorException result = assertThrows(InternalServerErrorException.class,
                ()-> colecaoService.deleterLivrosColecao(ID_USUARIO, idsLivros));
        // Então
        assertTrue(result.getMessage().contains("Ocorreu um erro ao remover o(s) livro(s) da coleção"));
    }

    @Test
    void listarLivrosTest() {
        // Dado
        when(colecaoRepository.findAll()).thenReturn(colecoes);
        // E
        when(informacaoService.buscarColecaoPorColecaoElivro(any(), any())).thenReturn(informacao);
        // Quando
        ResponseEntity<Page<LivroPage>> result = colecaoService.listarLivros(0, 10);
        // Então
        assertNotNull(result.getBody());
    }

    @Test
    void listarLivros2Test() {
        // Dado
        when(colecaoRepository.findAll()).thenReturn(colecoes);
        // E
        when(informacaoService.buscarColecaoPorColecaoElivro(any(), any())).thenReturn(null);
        // Quando
        ResponseEntity<Page<LivroPage>> result = colecaoService.listarLivros(0, 10);
        // Então
        assertNotNull(result.getBody());
    }
    @Test
    void listarLivrosExceptionTest() {
        // Dado
        when(colecaoRepository.findAll()).thenThrow(RuntimeException.class);
        // Quando
        InternalServerErrorException result = assertThrows(InternalServerErrorException.class,
                ()-> colecaoService.listarLivros(0, 10));
        // Então
        assertTrue(result.getMessage().contains("Ocorreu um erro ao listar os livros"));
    }

    @Test
    void listarColecaoPessoalTest() {
        // Dado
        when(colecaoRepository.findByIdUsuario(any())).thenReturn(colecao);
        // E
        when(informacaoService.buscarColecaoPorColecaoElivro(any(), any())).thenReturn(informacao);
        // Quando
        ResponseEntity<Page<LivroPage>> result =
                colecaoService.listarColecaoPessoal(0, 1, ID_USUARIO);
        // Então
        assertNotNull(result);
    }

    @Test
    void listarColecaoPessoal2Test() {
        // Dado
        when(colecaoRepository.findByIdUsuario(any())).thenReturn(colecao);
        // E
        when(informacaoService.buscarColecaoPorColecaoElivro(any(), any())).thenReturn(null);
        // Quando
        ResponseEntity<Page<LivroPage>> result =
                colecaoService.listarColecaoPessoal(0, 1, ID_USUARIO);
        // Então
        assertNotNull(result);
    }

    @Test
    void listarColecaoPessoalExceptionTest() {
        // Dado
        when(colecaoRepository.findByIdUsuario(any())).thenThrow(RuntimeException.class);
        // Quando
        InternalServerErrorException result =
                assertThrows(InternalServerErrorException.class,
                        ()->  colecaoService.listarColecaoPessoal(0, 1, ID_USUARIO));
        // Então
        assertTrue(result.getMessage().contains("Ocorreu um erro ao listar a coleção pessoal"));
    }
}