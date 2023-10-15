package com.bookexchange.msusuarios.controller;

import com.bookexchange.msusuarios.domain.representation.UsuarioInput;
import com.bookexchange.msusuarios.domain.representation.UsuarioOutput;
import com.bookexchange.msusuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/usuarios")
@Slf4j
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    @GetMapping
    public String status() {
        log.info("Obtendo o status do microservices de usuários.");
        return "OK";
    }
    @GetMapping(params = "id")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UsuarioOutput> getUsuarioPorId(@RequestParam("id") Integer id) {
        return usuarioService.getUsuarioPorId(id);
    }
    @PutMapping("/alterar")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UsuarioOutput> alterar(@RequestParam("id") Integer id,
                                               @RequestBody UsuarioInput usuarioInput) {
        return usuarioService.alterar(id, usuarioInput);
    }
}
