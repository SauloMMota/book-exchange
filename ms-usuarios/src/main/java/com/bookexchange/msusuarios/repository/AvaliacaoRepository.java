package com.bookexchange.msusuarios.repository;

import com.bookexchange.msusuarios.domain.Avaliacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {

    Page<Avaliacao> findAllByIdUsuarioAvaliado(Integer idUsuarioAvaliado, PageRequest pageRequest);
}
