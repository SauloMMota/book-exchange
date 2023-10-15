package com.bookexchange.msgerenciamentolivros.client;

import com.bookexchange.msgerenciamentolivros.config.FeignConfiguration;
import com.bookexchange.msgerenciamentolivros.domain.representation.UsuarioOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ms-usuarios", path = "/api/v1/usuarios", configuration = FeignConfiguration.class)
public interface UsuarioResourceClient {
    @GetMapping(params = "id")
    ResponseEntity<UsuarioOutput> getUsuarioPorId(@RequestParam("id") Integer id);
}
