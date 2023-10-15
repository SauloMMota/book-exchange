package com.bookexchange.msgerenciamentolivros.client;

import com.bookexchange.msgerenciamentolivros.config.FeignConfiguration;
import com.bookexchange.msgerenciamentolivros.domain.representation.LivroOutput;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ms-livros", path = "/api/v1/livros", configuration = FeignConfiguration.class)
public interface LivrosResourceClient {
    @GetMapping(params = "id")
    ResponseEntity<LivroOutput> obterLivroPorId(@RequestParam("id") Integer id);

}
