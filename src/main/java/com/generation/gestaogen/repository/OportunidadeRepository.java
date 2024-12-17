package com.generation.gestaogen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.gestaogen.model.Oportunidade;

public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long>{
	
	public List<Oportunidade> findAllByDescricaoContainingIgnoreCase(@Param("descricao") String descricao);

}