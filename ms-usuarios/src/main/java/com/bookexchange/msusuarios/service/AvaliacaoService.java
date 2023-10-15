package com.bookexchange.msusuarios.service;

import com.bookexchange.msusuarios.domain.Avaliacao;
import com.bookexchange.msusuarios.domain.Usuario;
import com.bookexchange.msusuarios.exception.EntidadeNaoEncontradaException;
import com.bookexchange.msusuarios.exception.InternalServerErrorException;
import com.bookexchange.msusuarios.repository.AvaliacaoRepository;
import com.bookexchange.msusuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final UsuarioRepository usuarioRepository;
    public ResponseEntity<Object> realizarAvaliacao(Integer idUsuarioAvaliador,
                                                    Integer idUsuarioAvaliado,
                                                    String comentario) {
        try {
            Usuario usuarioAvaliador = usuarioRepository.findById(idUsuarioAvaliador)
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário avaliador não encontrado."));

            Avaliacao avaliacao = Avaliacao.builder()
                    .idUsuarioAvaliado(idUsuarioAvaliado)
                    .idUsuarioAvaliador(usuarioAvaliador.getId())
                    .nomeUsuarioAvaliador(usuarioAvaliador.getNome())
                    .comentario(comentario)
                    .build();

            avaliacaoRepository.save(avaliacao);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (EntidadeNaoEncontradaException e) {
            throw new EntidadeNaoEncontradaException(e.getMessage());
        } catch(RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao fazer um comentário.", e);
        }
    }

    public ResponseEntity<Page<Avaliacao>> buscarAvaliacoes(Integer idUsuario,
                                                            Integer pagina,
                                                            Integer tamanhoPagina) {
        try {
            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            PageRequest pageRequest = PageRequest.of(pagina, tamanhoPagina, sort);
            Page<Avaliacao> avaliacoes = avaliacaoRepository.findAllByIdUsuarioAvaliado(idUsuario, pageRequest);
            return ResponseEntity.status(HttpStatus.OK).body(avaliacoes);
        } catch(RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao realizar a busca de avalições do usuário.", e);
        }
    }
}
