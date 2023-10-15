package com.bookexchange.mslivros.domain.representation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivroProjection {
    private Long id;
    private String autor;
    private String descricao;
    private String genero;
    private String titulo;
}
