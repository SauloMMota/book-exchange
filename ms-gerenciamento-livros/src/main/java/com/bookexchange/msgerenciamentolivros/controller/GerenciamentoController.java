package com.bookexchange.msgerenciamentolivros.controller;

import com.bookexchange.msgerenciamentolivros.service.GerenciamentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/gerenciamento")
@Slf4j
@RequiredArgsConstructor
public class GerenciamentoController {

    private final GerenciamentoService gerenciamentoService;

    @GetMapping
    public String status() {
        log.info("Obtendo o status do microservices de gerenciamento de livros.");
        return "OK";
    }

    @PostMapping("/solicitacao")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> realizarSolicitacaoTrocaLivro(@RequestParam Integer idUsuarioEmissor,
                                                                @RequestParam Integer idUsuarioReceptor,
                                                                @RequestParam Integer idLivroEmissor,
                                                                @RequestParam Integer idLivroReceptor,
                                                                @RequestBody String informacoes) {
        return gerenciamentoService.realizarSolicitacaoTrocaLivro(idUsuarioEmissor,
                idUsuarioReceptor, idLivroEmissor, idLivroReceptor, informacoes);
    }

    @PutMapping("confirmacao")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> confirmarTrocaDeLivro(@RequestParam Integer idSolicitacao) {
        return gerenciamentoService.confirmarTrocaDeLivro(idSolicitacao);
    }

    @GetMapping("solicitacoes")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getSolicitacoes(@RequestParam Integer idUsuario) {
        return gerenciamentoService.getSolicitacoes(idUsuario);
    }
}
