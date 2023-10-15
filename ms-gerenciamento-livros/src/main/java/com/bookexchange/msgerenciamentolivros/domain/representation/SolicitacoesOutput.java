package com.bookexchange.msgerenciamentolivros.domain.representation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolicitacoesOutput {

    private Integer idSolicitacao;
    private String nomeUsuarioEmissor;
    private String emailUsuarioEmissor;
    private Integer idLivroEmissor;
    private String tituloLivroEmissor;
    private Integer idLivroReceptor;
    private String tituloLivroReceptor;
    private Integer status;
    private String informacoes;
}
