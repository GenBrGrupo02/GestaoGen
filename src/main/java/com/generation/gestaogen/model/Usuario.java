package com.generation.gestaogen.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(min = 2, max = 100, message = "O atributo nome deve conter no mínimo 05 e no máximo 100 caracteres.")
	@NotNull(message = "O Atributo Nome é Obrigatório!")
	private String nome;

	@Size(min = 2, max = 100, message = "O atributo nome deve conter no mínimo 02 e no máximo 100 caracteres.")
	@NotNull(message = "O Atributo Usuário é Obrigatório!")
	@Email(message = "O Atributo Usuário deve ser um email válido!")
	@Schema(example = "email@email.com.br")
	private String usuario;

	@NotBlank(message = "O Atributo Senha é Obrigatório!")
	@Size(min = 8, message = "A Senha deve ter no mínimo 8 caracteres")
	private String senha;

	@Size(max = 5000, message = "O link da foto não pode ser maior do que 5000 caracteres")
	private String foto;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Oportunidade> oportunidades;

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

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public List<Oportunidade> getOportunidades() {
		return oportunidades;
	}

	public void setOportunidades(List<Oportunidade> oportunidades) {
		this.oportunidades = oportunidades;
	}
}
