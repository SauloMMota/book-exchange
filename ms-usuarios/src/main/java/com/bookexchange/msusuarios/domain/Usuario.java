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
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 150)
    private String nome;
    @Column(nullable = false, length = 150, unique = true)
    private String email;
    @Column(nullable = false, length = 150)
    private String senha;
    @Column(updatable = false)
    private LocalDate dataCadastro;
    @PrePersist
    public void prePersist() {
        setDataCadastro(LocalDate.now());
    }
}
