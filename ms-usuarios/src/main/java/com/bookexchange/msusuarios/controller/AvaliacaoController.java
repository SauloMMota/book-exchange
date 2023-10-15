package com.bookexchange.msusuarios.controller;

import com.bookexchange.msusuarios.domain.Avaliacao;
import com.bookexchange.msusuarios.service.AvaliacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/avaliacao")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> realizarAvaliacao(@RequestParam Integer idUsuarioAvaliador,
                                                    @RequestParam Integer idUsuarioAvaliado,
                                                    @RequestBody String comentario) {
        return avaliacaoService.realizarAvaliacao(idUsuarioAvaliador, idUsuarioAvaliado, comentario);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<Avaliacao>> buscarAvaliacoes(
            @RequestParam Integer idUsuario,
            @RequestParam(value = "page", defaultValue = "0") Integer pagina,
            @RequestParam(value = "size", defaultValue = "10") Integer tamanhoPagina) {
        return avaliacaoService.buscarAvaliacoes(idUsuario, pagina, tamanhoPagina);
    }
}
