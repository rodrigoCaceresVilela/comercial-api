package br.com.rcv.comercial.repository.lancamento;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.rcv.comercial.dto.LancamentoEstatisticaCategoria;
import br.com.rcv.comercial.dto.LancamentoEstatisticaDia;
import br.com.rcv.comercial.dto.LancamentoEstatisticaPessoa;
import br.com.rcv.comercial.model.Lancamento;
import br.com.rcv.comercial.repository.filter.LancamentoFilter;
import br.com.rcv.comercial.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {

	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia);
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia);
	public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim);
}
