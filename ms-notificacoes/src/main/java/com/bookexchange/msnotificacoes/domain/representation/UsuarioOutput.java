package com.bookexchange.msnotificacoes.domain.representation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioOutput {

    private Integer id;
    private String nome;
    private String email;

}
