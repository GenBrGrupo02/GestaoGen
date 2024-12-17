package com.generation.gestaogen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.generation.gestaogen.model.Oportunidade;
import com.generation.gestaogen.repository.ClienteRepository;
import com.generation.gestaogen.repository.OportunidadeRepository;

@Service
public class OportunidadeService {
	
	@Autowired
	private OportunidadeRepository oportunidadeRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;


	public void distribuirOportunidade(String oportunidadeDescricao, Long clienteId) {
		List<Oportunidade> oportunidades  = oportunidadeRepository.findAllByDescricaoContainingIgnoreCase(oportunidadeDescricao);
		
		for (Oportunidade oportunidade : oportunidades) {
			oportunidade.setPreenchida(true);
		}
		
		clienteRepository.findById(clienteId).get().setOportunidades(oportunidades);
		clienteRepository.findById(clienteId).get().setStatus(false);
	}
}