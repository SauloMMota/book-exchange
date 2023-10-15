package com.bookexchange.mslivros.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 20, nullable = false)
    private String titulo;
    @Column(length = 150, nullable = false)
    private String autor;
    @Column(length = 50, nullable = false)
    private String genero;
    @Column(length = 1000, nullable = false)
    private String descricao;

    @ManyToMany(mappedBy = "livros")
    @Builder.Default
    @JsonIgnore
    private List<Colecao> colecoes = new ArrayList<>();

}
