package com.bookexchange.msgerenciamentolivros.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Troca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer idUsuarioEmissor;

    private Integer idLivroEmissor;

    private Integer idUsuarioReceptor;

    private Integer idLivroReceptor;

    private Integer status;

    @Column(length = 3000)
    private String informacoes;

    @Column(updatable = false)
    private LocalDate dataSolicitacao;

    @Column(updatable = false)
    private LocalDate dataConfirmacao;

    @PrePersist
    public void prePersist() {
        setDataSolicitacao(LocalDate.now());
    }
}
