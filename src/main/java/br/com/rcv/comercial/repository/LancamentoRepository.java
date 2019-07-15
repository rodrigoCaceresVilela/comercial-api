package br.com.rcv.comercial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.rcv.comercial.model.Lancamento;
import br.com.rcv.comercial.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

}