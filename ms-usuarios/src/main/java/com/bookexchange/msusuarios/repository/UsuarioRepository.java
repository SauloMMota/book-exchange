package com.bookexchange.msusuarios.repository;

import com.bookexchange.msusuarios.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    String findByEmail(String email);

    @Query("SELECT usuario FROM Usuario usuario where usuario.email = :email")
    Optional<Usuario> findByEmailUsuario(@Param("email") String email);
}
