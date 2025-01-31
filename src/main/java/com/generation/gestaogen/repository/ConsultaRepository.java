package com.generation.gestaogen.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.generation.gestaogen.model.Consulta;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long>{
	
	public List<Consulta> findAllByDescricaoContainingIgnoreCase(@Param("descricao") String descricao);

}