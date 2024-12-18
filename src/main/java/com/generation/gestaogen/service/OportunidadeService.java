package com.generation.gestaogen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.gestaogen.model.Cliente;
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
		
		List<Oportunidade> oportunidades = oportunidadeRepository.findAllByDescricaoContainingIgnoreCase(oportunidadeDescricao);

	    Cliente cliente = clienteRepository.findById(clienteId)
	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

	    for (Oportunidade oportunidade : oportunidades) {
	        oportunidade.setPreenchida(true);
	        oportunidade.setCliente(cliente); 
	        oportunidadeRepository.save(oportunidade); 
	    }

	    cliente.setOportunidades(oportunidades);
	    cliente.setStatus(false);
	    clienteRepository.save(cliente);
	}
}