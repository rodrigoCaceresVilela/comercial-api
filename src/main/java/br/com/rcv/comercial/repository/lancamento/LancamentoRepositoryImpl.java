package br.com.rcv.comercial.repository.lancamento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import br.com.rcv.comercial.dto.LancamentoEstatisticaCategoria;
import br.com.rcv.comercial.dto.LancamentoEstatisticaDia;
import br.com.rcv.comercial.dto.LancamentoEstatisticaPessoa;
import br.com.rcv.comercial.model.Categoria_;
import br.com.rcv.comercial.model.Lancamento;
import br.com.rcv.comercial.model.Lancamento_;
import br.com.rcv.comercial.model.Pessoa_;
import br.com.rcv.comercial.repository.filter.LancamentoFilter;
import br.com.rcv.comercial.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestrições(lancamentoFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	private Long total(LancamentoFilter lancamentoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestrições(lancamentoFilter, builder, root);
		criteria.where(predicates);
		
		criteria.select(builder.count(root));
		
		return manager.createQuery(criteria).getSingleResult();
	}
	
	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		criteria.select(builder.construct(ResumoLancamento.class, 
						root.get(Lancamento_.id), root.get(Lancamento_.descricao),
						root.get(Lancamento_.dataVencimento), root.get(Lancamento_.dataPagamento),
						root.get(Lancamento_.valor), root.get(Lancamento_.tipo),
						root.get(Lancamento_.categoria).get(Categoria_.nome),
						root.get(Lancamento_.pessoa).get(Pessoa_.nome)));
		
		Predicate[] predicates = criarRestrições(lancamentoFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	public void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalDeRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalDeRegistrosPorPagina;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalDeRegistrosPorPagina);
	}
	
	private Predicate[] criarRestrições(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(builder.like(builder.lower(root.get(Lancamento_.descricao)), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}
		
		if (!StringUtils.isEmpty(lancamentoFilter.getDataVencimentoDe())) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoDe()));
		}
		
		if (!StringUtils.isEmpty(lancamentoFilter.getDataVencimentoAte())) {
			predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoAte()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<LancamentoEstatisticaCategoria> criteriaQuery = builder.createQuery(LancamentoEstatisticaCategoria.class);
		//Embora os dados buscados no DB serão apresentados pela DTO LancamentoEstatisticaCategoria, o root precisa ser do tipo Lancamento que é a Entity.
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(builder.construct(LancamentoEstatisticaCategoria.class, 
							root.get(Lancamento_.categoria),
							builder.sum(root.get(Lancamento_.valor))));
		
		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
		
		criteriaQuery.where(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), primeiroDia),
							builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), ultimoDia));
		
		criteriaQuery.groupBy(root.get(Lancamento_.categoria));
		
		TypedQuery<LancamentoEstatisticaCategoria> typedQuery = manager.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
	}
	
	@Override
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<LancamentoEstatisticaDia> criteriaQuery = builder.createQuery(LancamentoEstatisticaDia.class);
		//Embora os dados buscados no DB serão apresentados pela DTO LancamentoEstatisticaCategoria, o root precisa ser do tipo Lancamento que é a Entity.
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(builder.construct(LancamentoEstatisticaDia.class, 
							root.get(Lancamento_.tipo),
							root.get(Lancamento_.dataVencimento),
							builder.sum(root.get(Lancamento_.valor))));
		
		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
		
		criteriaQuery.where(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), primeiroDia),
							builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), ultimoDia));
		
		criteriaQuery.groupBy(root.get(Lancamento_.tipo),
							  root.get(Lancamento_.dataVencimento));
		
		TypedQuery<LancamentoEstatisticaDia> typedQuery = manager.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
	}
	
	@Override
	public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<LancamentoEstatisticaPessoa> criteriaQuery = builder.createQuery(LancamentoEstatisticaPessoa.class);
		//Embora os dados buscados no DB serão apresentados pela DTO LancamentoEstatisticaCategoria, o root precisa ser do tipo Lancamento que é a Entity.
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(builder.construct(LancamentoEstatisticaPessoa.class, 
							root.get(Lancamento_.tipo),
							root.get(Lancamento_.pessoa),
							builder.sum(root.get(Lancamento_.valor))));
		
		criteriaQuery.where(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), inicio),
							builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), fim));
		
		criteriaQuery.groupBy(root.get(Lancamento_.tipo),
							  root.get(Lancamento_.pessoa));
		
		TypedQuery<LancamentoEstatisticaPessoa> typedQuery = manager.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
	}
}
