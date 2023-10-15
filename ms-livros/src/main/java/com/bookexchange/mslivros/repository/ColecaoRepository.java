package com.bookexchange.mslivros.repository;

import com.bookexchange.mslivros.domain.Colecao;
import com.bookexchange.mslivros.domain.Livro;
import com.bookexchange.mslivros.domain.representation.LivroDTO;
import com.bookexchange.mslivros.domain.representation.LivroProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColecaoRepository extends JpaRepository<Colecao, Integer> {
    @Query("SELECT colecao FROM Colecao colecao " +
            " JOIN FETCH colecao.livros WHERE colecao.idUsuario = :idUsuario")
    Colecao findByIdUsuario(@Param("idUsuario") Integer idUsuario);

    @Query(value = "SELECT lv.id, lv.autor, lv.descricao, lv.genero, lv.titulo FROM colecao_livro cl \n" +
            " INNER JOIN livro lv ON lv.id = cl.livro_id \n" +
            " INNER JOIN colecao c ON c.id = cl.colecao_id \n" +
            " WHERE c.id_usuario = ?1 AND titulo LIKE CONCAT('%', ?2, '%') ", nativeQuery = true)
    List<LivroDTO> buscarLivros(Integer idUsuario, String search);

}
