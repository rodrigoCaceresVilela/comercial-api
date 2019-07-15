package br.com.rcv.comercial.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.rcv.comercial.model.Lancamento;
import br.com.rcv.comercial.repository.filter.LancamentoFilter;
import br.com.rcv.comercial.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {

	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
}
