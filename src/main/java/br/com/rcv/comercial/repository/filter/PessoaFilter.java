package br.com.rcv.comercial.repository.filter;

public class PessoaFilter {

	private String nome;
	private boolean ativo;

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}
