package com.bookexchange.mslivros.service;

import com.bookexchange.mslivros.domain.Colecao;
import com.bookexchange.mslivros.domain.Informacao;
import com.bookexchange.mslivros.domain.Livro;
import com.bookexchange.mslivros.domain.representation.MensagemOutput;
import com.bookexchange.mslivros.exception.EntidadeNaoEncontradaException;
import com.bookexchange.mslivros.exception.InternalServerErrorException;
import com.bookexchange.mslivros.exception.NegocioException;
import com.bookexchange.mslivros.repository.ColecaoRepository;
import com.bookexchange.mslivros.repository.InformacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class InformacaoService {

    private final InformacaoRepository informacaoRepository;
    private final ColecaoRepository colecaoRepository;
    public ResponseEntity<Object> incluirInformacao(Integer idUsuario, Integer idLivro, String detalhes) {
        try {

            Colecao colecao = colecaoRepository.findByIdUsuario(idUsuario);

            if(colecao == null || colecao.getLivros().isEmpty()) {
                throw new EntidadeNaoEncontradaException("Não existe coleção ou não tem livros na coleção para serem removidos.");
            }

            verificarExistenciaLivro(colecao, idLivro);
            Informacao informacao = informacaoRepository.findByIdColecaoAndIdLivro(colecao.getId(), idLivro);
            if(informacao != null) {
                informacao.setInformacao(detalhes);
            } else {
                 informacao = Informacao.builder()
                        .informacao(detalhes)
                        .idLivro(idLivro)
                        .idColecao(colecao.getId())
                        .build();
            }
            informacaoRepository.save(informacao);
            return ResponseEntity.status(HttpStatus.CREATED).body(MensagemOutput.builder()
                    .mensagem("Informação cadastrada com sucesso.").build());
        } catch (EntidadeNaoEncontradaException e) {
            throw new EntidadeNaoEncontradaException(e.getMessage());
        } catch (RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao adicionar informações.", e);
        }
    }

    public void removerInformacao(Integer idColecao, Set<Integer> idsLivros) {
        Set<Informacao> informacoes = new HashSet<>();
        idsLivros.forEach((Integer idLivro) -> {
            Informacao informacao = informacaoRepository.findByIdColecaoAndIdLivro(idColecao, idLivro);
            if (informacao != null) {
                informacoes.add(informacao);
            }
        });

        if (!informacoes.isEmpty()) {
            informacaoRepository.deleteAll(informacoes);
        }

    }

    public void verificarExistenciaLivro(Colecao colecao, Integer idLivro) {
        Boolean temOLivro = colecao.getLivros()
                .stream()
                .anyMatch((Livro livro) -> livro.getId().equals(idLivro));

        if(!temOLivro) {
            throw new NegocioException("Não existe o livro na colecação do usuário.");
        }
    }

    public Informacao buscarColecaoPorColecaoElivro(Integer idColecao, Integer idLivro) {
        return informacaoRepository.findByIdColecaoAndIdLivro(idColecao, idLivro);
    }
}
