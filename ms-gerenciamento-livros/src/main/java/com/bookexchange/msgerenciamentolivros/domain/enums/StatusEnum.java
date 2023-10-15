package com.bookexchange.msgerenciamentolivros.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {

    PENDENTE(1, "Pendente"),
    CONFIRMADA(2, "Confirmada"),
    CONCLUIDA(3, "Concluída");

    private Integer codigo;
    private String status;

    public static String getStatusPorCodigo(Integer codigo) {

        if(codigo == null) {
            return null;
        }

        for(StatusEnum s : values()) {
            if(codigo.equals(s.getCodigo())) {
                return s.getStatus();
            }
        }
        throw new IllegalArgumentException("Código inválido.");
    }
}
