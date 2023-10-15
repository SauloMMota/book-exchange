package com.bookexchange.mslivros.service;

import com.bookexchange.mslivros.domain.Colecao;
import com.bookexchange.mslivros.domain.Informacao;
import com.bookexchange.mslivros.domain.Livro;
import com.bookexchange.mslivros.domain.representation.LivroDTO;
import com.bookexchange.mslivros.domain.representation.LivroPage;
import com.bookexchange.mslivros.domain.representation.MensagemOutput;
import com.bookexchange.mslivros.exception.InternalServerErrorException;
import com.bookexchange.mslivros.exception.NegocioException;
import com.bookexchange.mslivros.repository.ColecaoRepository;
import com.bookexchange.mslivros.repository.InformacaoRepository;
import com.bookexchange.mslivros.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class ColecaoService {

    private final ColecaoRepository colecaoRepository;
    private final LivroRepository livroRepository;
    private final InformacaoService informacaoService;

    public ResponseEntity<Object> incluirLivroColecao(Set<Integer> idsLivros, Integer idUsuario) {
        try {
            List<Livro> livros = livroRepository.findAllById(idsLivros);
            // Verifico se já existe a colecao
            Colecao colecao = colecaoRepository.findByIdUsuario(idUsuario);
            if (colecao == null) {
                colecao = Colecao.builder()
                        .livros(new HashSet<>(livros))
                        .idUsuario(idUsuario)
                        .build();
            } else {
                colecao.getLivros().addAll(livros);
            }
            Colecao colecaoSalva = colecaoRepository.save(colecao);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(MensagemOutput
                            .builder()
                            .mensagem("O(s) livro(s) foi/foram salvo(s) na coleção.").build());
        } catch (RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao incluir livros na coleção.", e);
        }
    }

    public ResponseEntity<Object> deleterLivrosColecao(Integer idUsuario, Set<Integer> idsLivros) {
        try {
            if (idUsuario == null) {
                throw new NegocioException("Não foi informado o código do usuário.");
            }

            Colecao colecao = colecaoRepository.findByIdUsuario(idUsuario);

            if(colecao == null || colecao.getLivros().isEmpty()) {
                throw new NegocioException("Não existe coleção ou não tem livros na coleção para serem removidos.");
            }

            Set<Livro> listaAtualizada = colecao.getLivros()
                    .stream()
                    .filter((Livro livro) -> !idsLivros.contains(livro.getId())).collect(Collectors.toSet());

            colecao.setLivros(listaAtualizada);

            colecaoRepository.save(colecao);

            //Removendo Informacoes
            informacaoService.removerInformacao(colecao.getId(), idsLivros);

            return ResponseEntity.status(HttpStatus.OK).body(MensagemOutput
                    .builder()
                    .mensagem("Livro removido da colecão com sucesso.").build());
        } catch (NegocioException e) {
            throw new NegocioException(e.getMessage());
        } catch (RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao remover o(s) livro(s) da coleção.", e);
        }
    }

    public ResponseEntity<Page<LivroPage>> listarLivros(Integer pagina, Integer tamanhoPagina) {
        try {

            List<Colecao> colecoes = colecaoRepository.findAll();

            List<LivroPage> livrosPageList = colecoes.stream()
                    .flatMap(colecao -> {
                        if (Objects.nonNull(colecao.getLivros())) {
                            return colecao.getLivros().stream()
                                    .filter(Objects::nonNull)
                                    .map(livro -> {
                                        Informacao informacao = informacaoService.buscarColecaoPorColecaoElivro(colecao.getId(), livro.getId());
                                        String info = "";
                                        if (Objects.nonNull(informacao)) {
                                            info = informacao.getInformacao();
                                        }
                                        return LivroPage.builder()
                                                .idColecao(colecao.getId())
                                                .idUsuario(colecao.getIdUsuario())
                                                .idLivro(livro.getId())
                                                .titulo(livro.getTitulo())
                                                .autor(livro.getAutor())
                                                .genero(livro.getGenero())
                                                .descricao(livro.getDescricao())
                                                .informacao(info)
                                                .build();
                                    });
                        }
                        return Stream.empty();
                    })
                    .collect(Collectors.toList());
            /*Paginação através de uma lista*/
            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            PageRequest pageRequest = PageRequest.of(pagina, tamanhoPagina, sort);
            int start = Math.min((int)pageRequest.getOffset(), livrosPageList.size());
            int end = Math.min((start + pageRequest.getPageSize()), livrosPageList.size());

            Page<LivroPage> livrosPage = new PageImpl<>(livrosPageList.subList(start, end), pageRequest, livrosPageList.size());

            return ResponseEntity.status(HttpStatus.OK).body(livrosPage);
        } catch (RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao listar os livros. ", e);
        }
    }

    public ResponseEntity<Page<LivroPage>> listarColecaoPessoal(Integer pagina,
                                                                Integer tamanhoPagina,
                                                                Integer idUsuario) {
        try {

            Colecao colecao = colecaoRepository.findByIdUsuario(idUsuario);
            if(colecao != null && !colecao.getLivros().isEmpty()) {
                List<LivroPage> colecaoPessoalPageList = colecao.getLivros().stream().map((Livro livro) -> {
                    Informacao informacao = informacaoService.buscarColecaoPorColecaoElivro(colecao.getId(), livro.getId());
                    String info = "";
                    if (Objects.nonNull(informacao)) {
                        info = informacao.getInformacao();
                    }
                    return LivroPage.builder()
                            .idColecao(colecao.getId())
                            .idUsuario(colecao.getIdUsuario())
                            .idLivro(livro.getId())
                            .titulo(livro.getTitulo())
                            .autor(livro.getAutor())
                            .genero(livro.getGenero())
                            .descricao(livro.getDescricao())
                            .informacao(info)
                            .build();
                }).collect(Collectors.toList());

                Sort sort = Sort.by(Sort.Direction.ASC, "id");
                PageRequest pageRequest = PageRequest.of(pagina, tamanhoPagina, sort);
                int start = Math.min((int) pageRequest.getOffset(), colecaoPessoalPageList.size());
                int end = Math.min((start + pageRequest.getPageSize()), colecaoPessoalPageList.size());

                Page<LivroPage> livrosPage = new PageImpl<>(colecaoPessoalPageList.subList(start, end),
                        pageRequest, colecaoPessoalPageList.size());

                return ResponseEntity.status(HttpStatus.OK).body(livrosPage);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(Page.empty());
            }
        } catch(RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao listar a coleção pessoal.", e);
        }
    }

    public ResponseEntity<List<LivroDTO >> livrosAutoComplete(Integer idUsuario, String search) {
        try {
            List<LivroDTO> livros = colecaoRepository.buscarLivros(idUsuario, search);
            return ResponseEntity.status(HttpStatus.OK).body(livros);
        } catch (RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao buscar os livros.", e);
        }
    }
}
