package com.generation.gestaogen.controller;

import com.generation.gestaogen.model.UsuarioLogin;
import com.generation.gestaogen.service.UsuarioService;
import com.generation.gestaogen.model.Cliente;
import com.generation.gestaogen.model.Usuario;
import com.generation.gestaogen.repository.ClienteRepository;
import com.generation.gestaogen.repository.UsuarioRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
    @Autowired
    private ClienteRepository clienteRepository;

	@GetMapping
	public Iterable<Usuario> findAll() {
		return usuarioRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Usuario> getById(@PathVariable Long id) {
		return usuarioRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/logar")
	public ResponseEntity<UsuarioLogin> autenticarUsuario(@RequestBody Optional<UsuarioLogin> usuarioLogin) {
		return usuarioService.autenticarUsuario(usuarioLogin)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}

	@PostMapping("/cadastrar")
	public ResponseEntity<Usuario> postUsuario(@RequestBody @Valid Usuario usuario) {
		return usuarioService.cadastrarUsuario(usuario)
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	}

	@PutMapping("/atualizar")
	public ResponseEntity<Usuario> putUsuario(@Valid @RequestBody Usuario usuario) {
		return usuarioService.atualizarUsuario(usuario)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@PutMapping("vincular-cliente/{usuarioId}/{clienteId}")
	public ResponseEntity<Usuario> vincularClienteAUsuario(@PathVariable Long usuarioId, @PathVariable Long clienteId) {
	    Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
	    Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);

	    if (usuarioOpt.isEmpty() || clienteOpt.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    }

	    Usuario usuario = usuarioOpt.get();
	    Cliente cliente = clienteOpt.get();

	    if (!usuario.getClientes().contains(cliente)) {
	        usuario.getClientes().add(cliente); 
	        cliente.setUsuario(usuario); 
	        clienteRepository.save(cliente); 
	        usuarioRepository.save(usuario);
	    }

	    return ResponseEntity.status(HttpStatus.OK).body(usuario); 
	}
	
	@PutMapping("/remover-cliente/{usuarioId}/{clienteId}")
    public ResponseEntity<Usuario> removerClienteDoUsuario(@PathVariable Long usuarioId, @PathVariable Long clienteId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado"));

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        if (usuario.getClientes().contains(cliente)) {

            usuario.getClientes().remove(cliente);
            
            cliente.setUsuario(null);

            clienteRepository.save(cliente);
            
            usuarioRepository.save(usuario);

            return ResponseEntity.status(HttpStatus.OK).body(usuario);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); 
        }
    }

	
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Object> deleteUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuarioRepository.delete(usuario);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
