package com.bookexchange.msusuarios.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer idUsuarioAvaliador;

    private String nomeUsuarioAvaliador;

    private Integer idUsuarioAvaliado;

    @Column(length = 3000)
    private String comentario;

    @Column(updatable = false)
    private LocalDate dataAvaliacao;

    @PrePersist
    public void prePersist() {
        setDataAvaliacao(LocalDate.now());
    }
}
