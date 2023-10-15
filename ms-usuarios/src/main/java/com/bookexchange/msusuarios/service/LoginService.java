package com.bookexchange.msusuarios.service;

import com.bookexchange.msusuarios.assembler.UsuarioAssembler;
import com.bookexchange.msusuarios.domain.Usuario;
import com.bookexchange.msusuarios.domain.representation.LoginInput;
import com.bookexchange.msusuarios.domain.representation.LoginOutput;
import com.bookexchange.msusuarios.exception.InternalServerErrorException;
import com.bookexchange.msusuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioAssembler usuarioAssembler;

    public ResponseEntity<Object> logar(LoginInput loginInput) {
        try {

            Usuario usuario = usuarioRepository.findByEmailUsuario(loginInput.getEmail()).orElse(null);

            if(validarCredenciais(loginInput, usuario)) {
                return ResponseEntity.status(HttpStatus.OK).body(LoginOutput.builder()
                        .email(loginInput.getEmail())
                        .senha(loginInput.getSenha())
                        .mensagemErro("E-mail ou senha incorretos.")
                        .build());
            }

            return ResponseEntity.status(HttpStatus.OK)
                        .body(usuarioAssembler.toRepresentationOutput(usuario));

        } catch(RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao realizar o login.", e);
        }
    }

    public static boolean validarCredenciais(LoginInput loginInput, Usuario usuario) {
        return Objects.isNull(usuario)|| (!Objects.isNull(usuario) &&
                !loginInput.getSenha().equals(usuario.getSenha()));
    }
}
