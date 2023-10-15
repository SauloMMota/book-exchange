package com.bookexchange.mslivros.domain.representation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LivroOutput {

    private Integer id;
    private String titulo;
    private String autor;
    private String genero;
    private String descricao;
}
