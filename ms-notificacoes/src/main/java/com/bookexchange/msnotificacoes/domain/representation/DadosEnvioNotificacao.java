package com.bookexchange.msnotificacoes.domain.representation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DadosEnvioNotificacao {

    private UsuarioOutput usuarioReceptor;
    private LivroOutput livroReceptor;
    private UsuarioOutput usuarioEmissor;
    private LivroOutput livroEmissor;
    private String dataSolicitacao;
    private String status;
}
