package com.generation.gestaogen.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_consulta")
public class Consulta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "O Atributo Nome é obrigatório!")
	private String nome;

	private String descricao;
	
	@OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL)
	@JsonIgnoreProperties("consulta")
	private List<Cliente> clientes;

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public List<Cliente> getClientes() {
		return clientes;
	}


	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}
}

