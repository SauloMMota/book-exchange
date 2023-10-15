package com.bookexchange.mslivros.controller;


import com.bookexchange.mslivros.domain.Livro;
import com.bookexchange.mslivros.domain.representation.LivroDTO;
import com.bookexchange.mslivros.domain.representation.LivroPage;
import com.bookexchange.mslivros.domain.representation.LivroProjection;
import com.bookexchange.mslivros.service.ColecaoService;
import com.bookexchange.mslivros.service.InformacaoService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/livros")
@RequiredArgsConstructor
public class ColecaoController {

    private final ColecaoService colecaoService;
    private final InformacaoService informacaoService;

    @PostMapping("/colecao")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> cadastrar(@RequestBody Set<Integer> idsLivros,
                                            @RequestParam Integer idUsuario) {
        return colecaoService.incluirLivroColecao(idsLivros, idUsuario);
    }

    @PostMapping("/colecao/remocao")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> deletarLivrosColecao(@RequestParam Integer idUsuario,
                                                       @RequestBody Set<Integer> idsLivros) {
        return colecaoService.deleterLivrosColecao(idUsuario, idsLivros);
    }

    @PostMapping("/informacao")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> incluirInformacoes(@RequestParam Integer idUsuario,
                                                    @RequestParam Integer idLivro,
                                                    @RequestBody String detalhes) {
        return informacaoService.incluirInformacao(idUsuario, idLivro, detalhes);
    }

    @GetMapping("/colecoes")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<LivroPage>> listarLivrosColecoes(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return colecaoService.listarLivros(page, size);
    }

    @GetMapping("/colecaoPessoal")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<LivroPage>> listarColecaoPessoal(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam Integer idUsuario) {
        return colecaoService.listarColecaoPessoal(page, size, idUsuario);
    }

    @GetMapping("/autocomplete")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<LivroDTO>> livrosAutoComplete(@RequestParam Integer idUsuario,
                                                             @RequestParam String search) {
        return colecaoService.livrosAutoComplete(idUsuario, search);
    }
}
