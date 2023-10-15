package com.bookexchange.mslivros.exception.handler;

import com.bookexchange.mslivros.exception.EntidadeNaoEncontradaException;
import com.bookexchange.mslivros.exception.InternalServerErrorException;
import com.bookexchange.mslivros.exception.NegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;

class ApiExceptionHandlerTest {

    @InjectMocks
    private ApiExceptionHandler apiExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleNegocioException() {
        // Dado
        NegocioException negocioException = new NegocioException("Mensagem de erro");
        ResponseEntity<Object> responseEntity = apiExceptionHandler.handleNegocio(negocioException, webRequest);

        // Quando
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        // Então
        Problema problema = (Problema) responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), problema.getStatus());
        assertEquals("Mensagem de erro", problema.getTitulo());
    }

    @Test
    public void testHandleEntidadeNaoEncontradaException() {
        // Dado
        EntidadeNaoEncontradaException entidadeNaoEncontradaException =
                new EntidadeNaoEncontradaException("Entidade não encontrada");
        // Quando
        ResponseEntity<Object> responseEntity = apiExceptionHandler.handleEntidadeNaoEncontrada(entidadeNaoEncontradaException, webRequest);

        //Então
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        // Verificar se o corpo da resposta tem os dados corretos
        Problema problema = (Problema) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), problema.getStatus());
        assertEquals("Entidade não encontrada", problema.getTitulo());
    }

    @Test
    public void testHandleInternalServerErrorException() {
        // Dado
        InternalServerErrorException internalServerErrorException = new InternalServerErrorException("Erro interno do servidor",
                new Throwable());
        //Quando
        ResponseEntity<Object> responseEntity = apiExceptionHandler.handleInternalServerError(internalServerErrorException, webRequest);

        //Então
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        // Verificar se o corpo da resposta tem os dados corretos
        Problema problema = (Problema) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problema.getStatus());
        assertEquals("Erro interno do servidor", problema.getTitulo());
    }
}