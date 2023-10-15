package com.bookexchange.mslivros.domain.representation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LivroInput {

    private String titulo;
    private String autor;
    private String genero;
    private String descricao;

}
