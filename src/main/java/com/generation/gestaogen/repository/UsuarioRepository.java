package com.generation.gestaogen.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.gestaogen.model.Usuario;



public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
