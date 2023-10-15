package com.bookexchange.mslivros.repository;

import com.bookexchange.mslivros.domain.Informacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InformacaoRepository extends JpaRepository<Informacao, Integer> {

    Informacao findByIdColecaoAndIdLivro(Integer idColecao, Integer idLivro);
}
