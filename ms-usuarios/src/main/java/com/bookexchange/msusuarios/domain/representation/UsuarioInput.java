package com.bookexchange.msusuarios.domain.representation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioInput {

    private String nome;
    private String email;
    private String senha;

}
