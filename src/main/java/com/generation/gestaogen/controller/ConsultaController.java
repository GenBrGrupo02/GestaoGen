package com.generation.gestaogen.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.gestaogen.model.Cliente;
import com.generation.gestaogen.model.Consulta;
import com.generation.gestaogen.repository.ClienteRepository;
import com.generation.gestaogen.repository.ConsultaRepository;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/consulta")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConsultaController {
	
    @Autowired
    private ClienteRepository clienteRepository;
	
    @Autowired
    private ConsultaRepository consultaRepository;
    
    @GetMapping
    public ResponseEntity<List<Consulta>> getAll(){
        return ResponseEntity.ok(consultaRepository.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Consulta> getById(@PathVariable Long id){
        return consultaRepository.findById(id)
            .map(resposta -> ResponseEntity.ok(resposta))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    @GetMapping("/descricao/{descricao}")
    public ResponseEntity<List<Consulta>> getByDescricao(@PathVariable String descricao){
        return ResponseEntity.ok(consultaRepository.findAllByDescricaoContainingIgnoreCase(descricao));
    }
    
    @PostMapping
    public ResponseEntity<Consulta> post(@Valid @RequestBody Consulta consulta){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(consultaRepository.save(consulta));
    }
        
    @PutMapping
    public ResponseEntity<Consulta> put(@Valid @RequestBody Consulta consulta) {
        return consultaRepository.findById(consulta.getId()).map(existingConsulta -> {
            List<Cliente> clientesAtualizados = new ArrayList<>();
            
            if (consulta.getClientes() != null) {
                clientesAtualizados = consulta.getClientes().stream()
                    .map(cliente -> clienteRepository.findById(cliente.getId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
                
                for (Cliente cliente : clientesAtualizados) {
                    cliente.setConsulta(existingConsulta);
                }
            }

            existingConsulta.setNome(consulta.getNome());
            existingConsulta.setDescricao(consulta.getDescricao());
            existingConsulta.setClientes(clientesAtualizados);

            return ResponseEntity.status(HttpStatus.OK)
                .body(consultaRepository.save(existingConsulta));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    @PutMapping("vincular-consulta/{consultaId}/{clienteId}")
    public ResponseEntity<Consulta> vincularConsultaAClientes(@PathVariable Long consultaId, @PathVariable Long clienteId){
    	    Optional<Consulta> consultaOpt = consultaRepository.findById(consultaId);
    	    Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);

    	    if (consultaOpt.isEmpty() || clienteOpt.isEmpty()) {
    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    	    }

    	    Consulta consulta = consultaOpt.get();
    	    Cliente cliente = clienteOpt.get();

    	    if (!consulta.getClientes().contains(cliente)) {
    	        consulta.getClientes().add(cliente); 
    	        cliente.setConsulta(consulta); 
    	        clienteRepository.save(cliente); 
    	        consultaRepository.save(consulta);
    	    }

    	    return ResponseEntity.status(HttpStatus.OK).body(consulta); 
    }
    
    @PutMapping("/remover-cliente/{consultaId}")
    public ResponseEntity<Consulta> removerClientesDaConsulta(@PathVariable Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta não encontrada"));

        if (consulta.getClientes() != null && !consulta.getClientes().isEmpty()) {
            for (Cliente cliente : consulta.getClientes()) {
                cliente.setConsulta(null);
                clienteRepository.save(cliente);
            }

            consulta.getClientes().clear();
            
            consultaRepository.save(consulta);

            return ResponseEntity.status(HttpStatus.OK).body(consulta);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }
    
    @PutMapping("/remover-cliente/{consultaId}/{clienteId}")
    public ResponseEntity<Consulta> removerClienteDaConsulta(@PathVariable Long consultaId, @PathVariable Long clienteId) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta não encontrada"));

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        if (consulta.getClientes().contains(cliente)) {

            consulta.getClientes().remove(cliente);
            
            cliente.setConsulta(null);

            clienteRepository.save(cliente);
            
            consultaRepository.save(consulta);

            return ResponseEntity.status(HttpStatus.OK).body(consulta);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); 
        }
    }


    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Consulta> consulta = consultaRepository.findById(id);
        
        if(consulta.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        
        consultaRepository.deleteById(id);              
    }
}