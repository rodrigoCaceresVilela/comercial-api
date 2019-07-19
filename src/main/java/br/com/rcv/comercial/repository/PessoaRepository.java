package br.com.rcv.comercial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.rcv.comercial.model.Pessoa;
import br.com.rcv.comercial.repository.pessoa.PessoaRepositoryQuery;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQuery {

}
