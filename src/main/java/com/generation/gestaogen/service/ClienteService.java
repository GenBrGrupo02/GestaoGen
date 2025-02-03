package com.generation.gestaogen.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.gestaogen.model.Cliente;
import com.generation.gestaogen.model.Consulta;
import com.generation.gestaogen.repository.ClienteRepository;



@Service
public class ClienteService {
	
	
	@Autowired
	private ClienteRepository clienteRepository;


	
	
	public void mudarStatus(Long clienteId) {
	    Cliente cliente = clienteRepository.findById(clienteId)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
	    
	    boolean statusAtual = cliente.isStatus();
	    Optional<Consulta> opcionalPresente = Optional.ofNullable(cliente.getConsulta());
	    
	    boolean novoStatus = opcionalPresente.isPresent();
	    
	    if (statusAtual == novoStatus) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status não foi alterado.");
	    }
	    
	    cliente.setStatus(novoStatus);
	    clienteRepository.save(cliente);
	}

}