package com.bookexchange.msusuarios.domain.representation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginOutput {

    private String email;
    private String senha;
    private String mensagemErro;
}
