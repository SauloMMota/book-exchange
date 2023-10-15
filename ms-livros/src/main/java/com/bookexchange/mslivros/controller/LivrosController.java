package com.bookexchange.mslivros.controller;

import com.bookexchange.mslivros.domain.representation.LivroInput;
import com.bookexchange.mslivros.domain.representation.LivroOutput;
import com.bookexchange.mslivros.service.LivroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/livros")
@Slf4j
@RequiredArgsConstructor
public class LivrosController {

    private final LivroService livroService;
    @GetMapping
    public String status() {
        log.info("Obtendo o status do microservices de livros.");
        return "OK";
    }
    @PostMapping
    public ResponseEntity<Object> cadastrar(@RequestBody LivroInput livroInput) {
        return livroService.cadastrar(livroInput);
    }

    @GetMapping(params = "id")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LivroOutput> obterLivroPorId(@RequestParam("id") Integer id) {
        return livroService.obterLivroPorId(id);
    }

    @GetMapping(params = {"pagina", "tamanhoPagina"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<LivroOutput>> listarLivros(
            @RequestParam(value = "page", defaultValue = "0") Integer pagina,
            @RequestParam(value = "size", defaultValue = "10") Integer tamanhoPagina) {
        return livroService.listarLivros(pagina, tamanhoPagina);
    }

    @PutMapping("/atualizar")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LivroOutput> atualizarLivro(
            @RequestParam("id") Integer id, @RequestBody LivroInput livroInput) {
        return livroService.atualizarLivro(id, livroInput);
    }

    @DeleteMapping("/deletar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletarLivro(@RequestParam("id") Integer id) {
        return livroService.deletarLivro(id);
    }
}
