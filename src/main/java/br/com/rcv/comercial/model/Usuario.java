package br.com.rcv.comercial.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String email;
	
	@NotNull
	private String senha;
	
	@NotNull
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "permissoes_usuarios", 
					joinColumns = @JoinColumn(name = "usuario_id"), 
					inverseJoinColumns = @JoinColumn(name = "permissao_id"))
	private List<Permissao> permissoes;

//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
//	public String getEmail() {
//		return email;
//	}
//	public void setEmail(String email) {
//		this.email = email;
//	}
//	public String getSenha() {
//		return senha;
//	}
//	public void setSenha(String senha) {
//		this.senha = senha;
//	}
//	public List<Permissao> getPermissoes() {
//		return permissoes;
//	}
//	public void setPermissoes(List<Permissao> permissoes) {
//		this.permissoes = permissoes;
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Usuario other = (Usuario) obj;
//		if (id == null) {
//			if (other.id != null)
//				return false;
//		} else if (!id.equals(other.id))
//			return false;
//		return true;
//	}
}
