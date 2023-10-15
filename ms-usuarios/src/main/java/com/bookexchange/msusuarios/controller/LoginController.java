package com.bookexchange.msusuarios.controller;

import com.bookexchange.msusuarios.domain.representation.LoginInput;
import com.bookexchange.msusuarios.domain.representation.UsuarioInput;
import com.bookexchange.msusuarios.domain.representation.UsuarioOutput;
import com.bookexchange.msusuarios.service.LoginService;
import com.bookexchange.msusuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> logar(@RequestBody LoginInput loginInput) {
        return loginService.logar(loginInput);
    }

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UsuarioOutput> cadastrar(@RequestBody UsuarioInput usuarioInput) {
        return usuarioService.cadastrar(usuarioInput);
    }

}
