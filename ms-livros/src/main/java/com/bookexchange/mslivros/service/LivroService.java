package com.bookexchange.mslivros.service;

import com.bookexchange.mslivros.assembler.LivroAssembler;
import com.bookexchange.mslivros.domain.Colecao;
import com.bookexchange.mslivros.domain.Informacao;
import com.bookexchange.mslivros.domain.Livro;
import com.bookexchange.mslivros.domain.representation.LivroInput;
import com.bookexchange.mslivros.domain.representation.LivroOutput;
import com.bookexchange.mslivros.domain.representation.MensagemOutput;
import com.bookexchange.mslivros.exception.InternalServerErrorException;
import com.bookexchange.mslivros.exception.NegocioException;
import com.bookexchange.mslivros.repository.ColecaoRepository;
import com.bookexchange.mslivros.repository.InformacaoRepository;
import com.bookexchange.mslivros.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final LivroAssembler livroAssembler;
    private final ColecaoRepository colecaoRepository;
    private final InformacaoRepository informacaoRepository;
    public ResponseEntity<Object> cadastrar(LivroInput livroInput) {
        try {
            Livro livro = livroAssembler.toModel(livroInput);
            livro = livroRepository.save(livro);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(livroAssembler.toRepresentation(livro));
        } catch (RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao cadastrar o livro.", e);
        }
    }

    public ResponseEntity<LivroOutput> obterLivroPorId(Integer id) {
        try {
            Livro livro = buscarLivroPorId(id);
            return ResponseEntity.status(HttpStatus.OK).body(livroAssembler.toRepresentation(livro));
        } catch (NegocioException e) {
            throw new NegocioException(e.getMessage());
        } catch(RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao buscar livro.", e);
        }
    }

    public ResponseEntity<Page<LivroOutput>> listarLivros(Integer pagina, Integer tamanhoPagina) {
        try {
            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            PageRequest pageRequest = PageRequest.of(pagina, tamanhoPagina, sort);
            Page<Livro> livros = livroRepository.findAll(pageRequest);
            return ResponseEntity.status(HttpStatus.OK).body(livroAssembler.toPageRepresentation(livros));
        } catch (RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao listar os livros. ", e);
        }
    }

    public ResponseEntity<LivroOutput> atualizarLivro(Integer id, LivroInput livroInput) {
        try {

            Livro livro = buscarLivroPorId(id);

            livro.setAutor(livroInput.getAutor());
            livro.setGenero(livroInput.getGenero());
            livro.setDescricao(livroInput.getDescricao());
            livro.setTitulo(livroInput.getTitulo());

            Livro livroAtualizado = livroRepository.save(livro);
            return ResponseEntity.status(HttpStatus.OK).body(livroAssembler.toRepresentation(livroAtualizado));
        } catch (NegocioException e) {
            throw new NegocioException(e.getMessage());
        } catch (RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao atualizar as informações do livro.", e);
        }
    }

    public ResponseEntity<Void> deletarLivro(Integer id) {
        try {
            Livro livro = buscarLivroPorId(id);

            List<Colecao> colecoes = colecaoRepository.findAll();

            // Remover o livro de todas as coleções
            List<Colecao> colecoesAtualizadas = colecoes.stream().filter((Colecao colecao) ->
                colecao.getLivros().remove(livro)
            ).collect(Collectors.toList());

            List<Informacao> informacoes = new ArrayList<>();

            colecoesAtualizadas.forEach((Colecao colecao) -> colecao.getLivros().forEach((Livro l) ->{
                    Informacao info = informacaoRepository.findByIdColecaoAndIdLivro(colecao.getId(), l.getId());
                    if(info != null) {
                        informacoes.add(info);
                    }
            }));
            informacaoRepository.deleteAll(informacoes);
            colecaoRepository.saveAll(colecoesAtualizadas);

            livroRepository.delete(livro);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(NegocioException e) {
            throw new NegocioException(e.getMessage());
        } catch(RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao deletar o livro.", e);
        }
    }

    public Livro buscarLivroPorId(Integer id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new NegocioException("O livro não foi encontrado."));
    }
}
