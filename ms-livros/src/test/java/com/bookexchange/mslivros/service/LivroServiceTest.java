package com.bookexchange.mslivros.service;

import com.bookexchange.mslivros.assembler.LivroAssembler;
import com.bookexchange.mslivros.domain.Livro;
import com.bookexchange.mslivros.domain.representation.LivroInput;
import com.bookexchange.mslivros.domain.representation.LivroOutput;
import com.bookexchange.mslivros.exception.InternalServerErrorException;
import com.bookexchange.mslivros.exception.NegocioException;
import com.bookexchange.mslivros.repository.ColecaoRepository;
import com.bookexchange.mslivros.repository.InformacaoRepository;
import com.bookexchange.mslivros.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class LivroServiceTest {
    private LivroRepository livroRepository;
    private LivroAssembler livroAssembler;
    private LivroService livroService;

    private ColecaoRepository colecaoRepository;
    private InformacaoRepository informacaoRepository;
    private LivroInput livroInput;
    private LivroOutput livroOutput;
    private Livro livro;
    private  Page<Livro> livrosPage;

    @BeforeEach
    void setUp() {
        livroRepository = mock(LivroRepository.class);
        livroAssembler = mock(LivroAssembler.class);

        livroService = new LivroService(livroRepository, livroAssembler, colecaoRepository, informacaoRepository);

        livroInput = LivroInput.builder()
                .titulo("Test")
                .autor("Test")
                .genero("Test")
                .descricao("Test")
                .build();

        livro = Livro.builder()
                .id(1)
                .autor("Test")
                .titulo("Test")
                .genero("Test")
                .descricao("Test")
                .build();

        livroOutput = LivroOutput.builder()
                .autor(livro.getAutor())
                .titulo(livro.getTitulo())
                .id(livro.getId())
                .genero(livro.getGenero())
                .descricao(livro.getDescricao())
                .build();

        livrosPage = Page.empty(Pageable.ofSize(1));
    }

    @Test
    void cadastrarTest() {
        // Dado
        when(livroAssembler.toModel(any())).thenReturn(livro);
        // E
        when(livroRepository.save(any())).thenReturn(livro);
        // Quando
        ResponseEntity<Object> result = livroService.cadastrar(livroInput);
        // Então
        assertNotNull(result.getBody());
    }

    @Test
    void cadastrarExceptionTest() {
        // Dado
        when(livroAssembler.toModel(any())).thenReturn(livro);
        // E
        when(livroRepository.save(any())).thenThrow(RuntimeException.class);
        // Quando
        InternalServerErrorException result = assertThrows(InternalServerErrorException.class,
                () -> livroService.cadastrar(livroInput));
        // Então
        assertTrue(result.getMessage().contains("Ocorreu um erro ao cadastrar o livro"));
    }

    @Test
    void obterLivroPorIdTest() {
        // Dado
        when(livroRepository.findById(any())).thenReturn(Optional.of(livro));
        //E
        when(livroAssembler.toRepresentation(any())).thenReturn(livroOutput);
        // Quando
        ResponseEntity<LivroOutput> result = livroService.obterLivroPorId(1);
        // Então
        assertNotNull(result);
    }

    @Test
    void obterLivroPorIdNegocioExceptionTest() {
        // Dado
        when(livroRepository.findById(any())).thenReturn(Optional.empty());
        // Quando
        NegocioException result = assertThrows(NegocioException.class,
                () -> livroService.obterLivroPorId(1));
        // Então
        assertTrue(result.getMessage().contains("O livro não foi encontrado"));
    }

    @Test
    void obterLivroPorIdExceptionTest() {
        // Dado
        when(livroRepository.findById(any())).thenThrow(RuntimeException.class);
        // Quando
        RuntimeException result = assertThrows(RuntimeException.class,
                () -> livroService.obterLivroPorId(1));
        // Então
        assertTrue(result.getMessage().contains("Ocorreu um erro ao buscar livro"));
    }

    @Test
    void listarLivrosTest() {
        // Dado
        when(livroRepository.findAll(any(PageRequest.class))).thenReturn(livrosPage);
        // Quando
        ResponseEntity<Page<LivroOutput>> result = livroService.listarLivros(0, 10);
        // Então
        assertNotNull(result);
    }

    @Test
    void listarLivrosExceptionTest() {
        // Dado
        when(livroRepository.findAll(any(PageRequest.class))).thenThrow(RuntimeException.class);
        // Quando
        InternalServerErrorException result = assertThrows(InternalServerErrorException.class,
                () -> livroService.listarLivros(0, 10));
        // Então
        assertTrue(result.getMessage().contains("Ocorreu um erro ao listar os livros"));
    }

    @Test
    void atualizarLivroTest() {
        // Dado
        when(livroRepository.findById(any())).thenReturn(Optional.of(livro));
        // E
        when(livroRepository.save(any())).thenReturn(livro);
        // Quando
        ResponseEntity<LivroOutput> result = livroService.atualizarLivro(1,livroInput);
        // Então
        assertNotNull(result);
    }

    @Test
    void atualizarLivroNegocioExceptionTest() {
        // Dado
        when(livroRepository.findById(any())).thenReturn(Optional.empty());
        // Quando
        NegocioException result = assertThrows(NegocioException.class,
                () -> livroService.atualizarLivro(1,livroInput));
        // Então
        assertTrue(result.getMessage().contains("O livro não foi encontrado"));
    }

    @Test
    void atualizarLivroExceptionTest() {
        // Dado
        when(livroRepository.findById(any())).thenThrow(RuntimeException.class);
        // Quando
        InternalServerErrorException result = assertThrows(InternalServerErrorException.class,
                () -> livroService.atualizarLivro(1,livroInput));
        // Então
        assertTrue(result.getMessage().contains("Ocorreu um erro ao atualizar as informações do livro"));
    }

    @Test
    void deletarLivroTest() {
        // Dado
        when(livroRepository.findById(any())).thenReturn(Optional.of(livro));
        // E
        doNothing().when(livroRepository).delete(any());
        // Quando
        livroService.deletarLivro(1);
        // Então
        verify(livroRepository, times(1)).findById(any());
    }

    @Test
    void deletarLivroNegocioExceptionTest() {
        // Dado
        when(livroRepository.findById(any())).thenReturn(Optional.empty());
        // Quando
        NegocioException result = assertThrows(NegocioException.class,
                () -> livroService.deletarLivro(1));
        // Então
        assertTrue(result.getMessage().contains("O livro não foi encontrado"));
    }

    @Test
    void deletarLivroExceptionTest() {
        // Dado
        when(livroRepository.findById(any())).thenThrow(RuntimeException.class);
        // Quando
        InternalServerErrorException result = assertThrows(InternalServerErrorException.class,
                () -> livroService.deletarLivro(1));
        // Então
        assertTrue(result.getMessage().contains("Ocorreu um erro ao deletar o livro"));
    }
}