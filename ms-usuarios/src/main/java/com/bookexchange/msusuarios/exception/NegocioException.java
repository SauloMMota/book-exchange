package com.bookexchange.msusuarios.exception;

public class NegocioException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public NegocioException(String mensagem) {
        super(mensagem);
    }
}
