package br.com.rcv.comercial.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.rcv.comercial.model.Pessoa;
import br.com.rcv.comercial.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Pessoa atualizar(Long id, Pessoa pessoa) {
		Pessoa pessoaSalva = findById(id);

		pessoaSalva.getContatos().clear();
		pessoaSalva.getContatos().addAll(pessoa.getContatos());
		pessoaSalva.getContatos().forEach(c -> c.setPessoa(pessoaSalva));
		BeanUtils.copyProperties(pessoa, pessoaSalva, "id", "contatos");

		return pessoaRepository.saveAndFlush(pessoaSalva);
	}

	private Pessoa findById(Long id) {
		Pessoa pessoaSalva = pessoaRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
		
		return pessoaSalva;
	}

	public void atualizarPropriedadeAtivo(Long id, Boolean ativo) {
		Pessoa pessoaSalva = findById(id);
		pessoaSalva.setAtivo(ativo);
		
		pessoaRepository.flush();
	}

	public Pessoa save(Pessoa pessoa) {
		pessoa.getContatos().forEach(c -> c.setPessoa(pessoa));
		
		return pessoaRepository.save(pessoa);
	}
}