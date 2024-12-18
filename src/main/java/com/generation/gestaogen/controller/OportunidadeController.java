package com.generation.gestaogen.controller;

import java.util.List;
import java.util.Optional;

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

import com.generation.gestaogen.model.Oportunidade;
import com.generation.gestaogen.repository.OportunidadeRepository;
import com.generation.gestaogen.service.OportunidadeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/oportunidade")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OportunidadeController {
	
	@Autowired
    private OportunidadeService service;
    
    @Autowired
    private OportunidadeRepository oportunidadeRepository;
    
    @GetMapping
    public ResponseEntity<List<Oportunidade>> getAll(){
        return ResponseEntity.ok(oportunidadeRepository.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Oportunidade> getById(@PathVariable Long id){
        return oportunidadeRepository.findById(id)
            .map(resposta -> ResponseEntity.ok(resposta))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    @GetMapping("/descricao/{descricao}")
    public ResponseEntity<List<Oportunidade>> getByDescricao(@PathVariable String descricao){
        return ResponseEntity.ok(oportunidadeRepository.findAllByDescricaoContainingIgnoreCase(descricao));
    }
    
    @PostMapping
    public ResponseEntity<Oportunidade> post(@Valid @RequestBody Oportunidade oportunidade){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(oportunidadeRepository.save(oportunidade));
    }
    
    @PostMapping("/distribuir/{descricao}/{id}")
    public ResponseEntity<String> distribuir(@PathVariable String descricao, @PathVariable Long id){
    	try {
    		service.distribuirOportunidade(descricao, id);
        	return ResponseEntity.ok("Distribuição de oportunidade realizada!");
    	}catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
    
    @PutMapping
    public ResponseEntity<Oportunidade> put(@Valid @RequestBody Oportunidade oportunidade){
        return oportunidadeRepository.findById(oportunidade.getId())
            .map(resposta -> ResponseEntity.status(HttpStatus.CREATED)
            .body(oportunidadeRepository.save(oportunidade)))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Oportunidade> oportunidade = oportunidadeRepository.findById(id);
        
        if(oportunidade.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        
        oportunidadeRepository.deleteById(id);              
    }
}