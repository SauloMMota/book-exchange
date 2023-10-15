package com.bookexchange.msusuarios.service;

import com.bookexchange.msusuarios.assembler.UsuarioAssembler;
import com.bookexchange.msusuarios.domain.Usuario;
import com.bookexchange.msusuarios.domain.representation.UsuarioInput;
import com.bookexchange.msusuarios.domain.representation.UsuarioOutput;
import com.bookexchange.msusuarios.exception.EntidadeNaoEncontradaException;
import com.bookexchange.msusuarios.exception.InternalServerErrorException;
import com.bookexchange.msusuarios.exception.NegocioException;
import com.bookexchange.msusuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioAssembler usuarioAssembler;
    public ResponseEntity<UsuarioOutput> cadastrar(UsuarioInput usuarioInput) {
        try {
            verificarEmail(usuarioInput.getEmail());

            Usuario usuario = usuarioAssembler.toModel(usuarioInput);
            Usuario usuarioSalvo = usuarioRepository.save(usuario);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(usuarioAssembler.toRepresentationOutput(usuarioSalvo));
        } catch (NegocioException e) {
            throw new NegocioException(e.getMessage());
        } catch(RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao cadastrar o usuário.", e);
        }
    }

    public ResponseEntity<UsuarioOutput> getUsuarioPorId(Integer id) {
        Usuario usuario = buscarUsuario(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usuarioAssembler.toRepresentationOutput(usuario));
    }

    public Usuario buscarUsuario(Integer id) {
        return usuarioRepository.findById(id).orElseThrow(()->
                new EntidadeNaoEncontradaException("Usuário não encontrado"));
    }

    public ResponseEntity<UsuarioOutput> alterar(Integer id, UsuarioInput usuarioInput) {
        try {
            Usuario usuario = buscarUsuario(id);
            usuario.setSenha(usuarioInput.getSenha());

            verficarEmailAtualizacao(usuario);
            usuario.setEmail(usuarioInput.getEmail());

            Usuario alterado = usuarioRepository.save(usuario);

            return ResponseEntity.status(HttpStatus.OK).body(usuarioAssembler.toRepresentationOutput(alterado));
        } catch (NegocioException e) {
            throw new NegocioException(e.getMessage());
        } catch (RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao alterar os dados do usuário.", e);
        }
    }

    private void verificarEmail(String email) {

        String emailConsultado = usuarioRepository.findByEmail(email);

        if (StringUtils.isNotEmpty(emailConsultado)) {
            throw new NegocioException("O email informado já está em uso.");
        }
    }

    private void verficarEmailAtualizacao(Usuario usuario) {
        Usuario usuarioEmail = usuarioRepository.findByEmailUsuario(usuario.getEmail()).orElse(null);
        if(usuarioEmail != null &&
                usuarioEmail.getEmail().equals(usuario.getEmail()) &&
                !usuarioEmail.getId().equals(usuario.getId())) {
            throw new NegocioException("O email informado já está em uso.");
        }
    }
}
