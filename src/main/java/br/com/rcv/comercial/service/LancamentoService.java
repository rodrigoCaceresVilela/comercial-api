package br.com.rcv.comercial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rcv.comercial.model.Lancamento;
import br.com.rcv.comercial.model.Pessoa;
import br.com.rcv.comercial.repository.LancamentoRepository;
import br.com.rcv.comercial.repository.PessoaRepository;
import br.com.rcv.comercial.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	public Lancamento salvar(Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.findById(lancamento.getPessoa().getId()).orElseThrow(() -> new PessoaInexistenteOuInativaException());
		
		if(pessoa.isInativo()) throw new PessoaInexistenteOuInativaException();
		
		return lancamentoRepository.save(lancamento);
	}
}
