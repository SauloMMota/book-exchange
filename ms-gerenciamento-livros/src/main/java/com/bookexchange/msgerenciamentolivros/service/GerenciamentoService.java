package com.bookexchange.msgerenciamentolivros.service;

import com.bookexchange.msgerenciamentolivros.client.LivrosResourceClient;
import com.bookexchange.msgerenciamentolivros.client.UsuarioResourceClient;
import com.bookexchange.msgerenciamentolivros.domain.Troca;
import com.bookexchange.msgerenciamentolivros.domain.enums.StatusEnum;
import com.bookexchange.msgerenciamentolivros.domain.representation.*;
import com.bookexchange.msgerenciamentolivros.exception.EntidadeNaoEncontradaException;
import com.bookexchange.msgerenciamentolivros.exception.InternalServerErrorException;
import com.bookexchange.msgerenciamentolivros.exception.NegocioException;
import com.bookexchange.msgerenciamentolivros.mqueue.EnvioNotificacaoPublisher;
import com.bookexchange.msgerenciamentolivros.repository.TrocaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GerenciamentoService {

    private final LivrosResourceClient livrosResourceClient;
    private final UsuarioResourceClient usuarioResourceClient;
    private final TrocaRepository trocaRepository;
    private final EnvioNotificacaoPublisher envioNotificacaoPublisher;
    private LivroOutput obterLivro(Integer idLivro) {
        //Obter dados do MS-Livros
        ResponseEntity<LivroOutput> livroResponse = livrosResourceClient.obterLivroPorId(idLivro);
        if (!Objects.nonNull(livroResponse.getBody())) {
            throw new EntidadeNaoEncontradaException("Não foi possível encontra o livro");
        }
        return livroResponse.getBody();
    }

    private UsuarioOutput obterUsuario(Integer idUsuario) {
        //Obter dados do MS-Usuarios
        ResponseEntity<UsuarioOutput> usuarioResponse = usuarioResourceClient.getUsuarioPorId(idUsuario);
        if (!Objects.nonNull(usuarioResponse.getBody())) {
            throw new EntidadeNaoEncontradaException("Não foi possível encontra o usuário.");
        }
        return usuarioResponse.getBody();
    }
    public ResponseEntity<Object> realizarSolicitacaoTrocaLivro(Integer idUsuarioEmissor,
                                                                Integer idUsuarioReceptor,
                                                                Integer idLivroEmissor,
                                                                Integer idLivroReceptor,
                                                                String informacoes) {
        try {

            LivroOutput livroReceptor = obterLivro(idLivroReceptor);
            LivroOutput livroEmissor = obterLivro(idLivroEmissor);

            UsuarioOutput usuarioReceptor = obterUsuario(idUsuarioReceptor);
            UsuarioOutput usuarioEmissor = obterUsuario(idUsuarioEmissor);

            Troca troca = Troca.builder()
                    .idUsuarioEmissor(usuarioEmissor.getId())
                    .idLivroEmissor(livroEmissor.getId())
                    .idUsuarioReceptor(usuarioReceptor.getId())
                    .idLivroReceptor(livroReceptor.getId())
                    .status(StatusEnum.PENDENTE.getCodigo())
                    .informacoes(informacoes)
                    .build();

            Troca trocaSalva = trocaRepository.save(troca);
            envioNotificacaoPublisher.enviarDadosNotificacao(montarDados(livroReceptor,
                    livroEmissor,
                    usuarioReceptor,
                    usuarioEmissor,
                    trocaSalva));
            return ResponseEntity.status(HttpStatus.CREATED).body(MensagemOutput
                    .builder().mensagem("Solicitação de troca realizado com sucesso!").build());
        } catch (NegocioException e) {
            throw new EntidadeNaoEncontradaException(e.getMessage());
        } catch (RuntimeException | JsonProcessingException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao realização de troca de livros.", e);
        }
    }

    public DadosEnvioNotificacao montarDados(LivroOutput livroReceptor,
                                             LivroOutput livroEmissor,
                                             UsuarioOutput usuarioReceptor,
                                             UsuarioOutput usuarioEmissor,
                                             Troca trocaSalva) {


        return DadosEnvioNotificacao.builder()
                .livroReceptor(livroReceptor)
                .livroEmissor(livroEmissor)
                .usuarioEmissor(usuarioEmissor)
                .usuarioReceptor(usuarioReceptor)
                .status(StatusEnum.getStatusPorCodigo(trocaSalva.getStatus()))
                .dataSolicitacao(trocaSalva.getDataSolicitacao().toString())
                .build();
    }

    public ResponseEntity<Object> confirmarTrocaDeLivro(Integer idSolicitacao) {
        try {

            Troca troca = trocaRepository.findById(idSolicitacao)
                    .orElseThrow(() ->
                            new EntidadeNaoEncontradaException("Não existe uma solicitação de troca de livros."));

            troca.setStatus(StatusEnum.CONFIRMADA.getCodigo());
            troca.setDataConfirmacao(LocalDate.now());

            trocaRepository.save(troca);
            return ResponseEntity.status(HttpStatus.OK).body(MensagemOutput
                    .builder().mensagem("Confirmação realizada com sucesso").build());
        } catch (EntidadeNaoEncontradaException e) {
            throw new EntidadeNaoEncontradaException(e.getMessage());
        } catch (RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao confirmar a troca de livro.", e);
        }
    }

    public ResponseEntity<Object> getSolicitacoes(Integer idUsario) {
        try{

            List<Troca> trocas = trocaRepository.findAllByIdUsuarioReceptor(idUsario);
            List<SolicitacoesOutput> solicitacoes = trocas.stream().map((Troca troca) -> {
                LivroOutput livroReceptor = obterLivro(troca.getIdLivroReceptor());
                LivroOutput livroEmissor = obterLivro(troca.getIdLivroEmissor());

                UsuarioOutput usuarioEmissor = obterUsuario(troca.getIdUsuarioEmissor());

                return SolicitacoesOutput.builder()
                        .idSolicitacao(troca.getId())
                        .status(troca.getStatus())
                        .tituloLivroEmissor(livroEmissor.getTitulo())
                        .idLivroEmissor(livroEmissor.getId())
                        .tituloLivroReceptor(livroReceptor.getTitulo())
                        .idLivroReceptor(livroReceptor.getId())
                        .nomeUsuarioEmissor(usuarioEmissor.getNome())
                        .emailUsuarioEmissor(usuarioEmissor.getEmail())
                        .informacoes(troca.getInformacoes())
                        .build();

            }).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(solicitacoes);
        } catch(RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao buscar as solicitações.", e);
        }

    }
}
