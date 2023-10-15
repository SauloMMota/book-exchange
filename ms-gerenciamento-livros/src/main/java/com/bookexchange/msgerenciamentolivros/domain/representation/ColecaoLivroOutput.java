package com.bookexchange.msgerenciamentolivros.domain.representation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColecaoLivroOutput {

    private Integer id;
    private Integer idUsuario;
    private List<LivroOutput> livros;

}
