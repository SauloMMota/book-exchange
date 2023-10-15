package com.bookexchange.msgerenciamentolivros.repository;

import com.bookexchange.msgerenciamentolivros.domain.Troca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrocaRepository extends JpaRepository<Troca, Integer> {

    Optional<Troca> findByIdUsuarioReceptor(Integer idUsuarioReceptor);

    List<Troca> findAllByIdUsuarioReceptor(Integer idUsuarioReceptor);
}
