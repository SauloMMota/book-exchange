package com.bookexchange.msusuarios.exception;

public class InternalServerErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InternalServerErrorException(String mensagem) {
        super(mensagem);
    }
    public InternalServerErrorException(String mensagem, Throwable e) {
        super(mensagem, e);
    }

}
