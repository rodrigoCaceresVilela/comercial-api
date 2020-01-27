package br.com.rcv.comercial.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.rcv.comercial.dto.LancamentoEstatisticaPessoa;
import br.com.rcv.comercial.mail.Mailer;
import br.com.rcv.comercial.model.Lancamento;
import br.com.rcv.comercial.model.Pessoa;
import br.com.rcv.comercial.model.Usuario;
import br.com.rcv.comercial.repository.LancamentoRepository;
import br.com.rcv.comercial.repository.PessoaRepository;
import br.com.rcv.comercial.repository.UsuarioRepository;
import br.com.rcv.comercial.service.exception.PessoaInexistenteOuInativaException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class LancamentoService {
	
	private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private Mailer mailer;
	
	private Logger logger = LoggerFactory.getLogger(LancamentoService.class);
	
	/**
	 * @apiNote
	 * Anotação usada para enviar e-mail agendado assim que a aplicação é iniciada com intervalo de 30 minutos.
	 */
//	@Scheduled(fixedDelay = 1000 * 60 * 30)
	public void avisarSobreLancamentosVencidos() {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Preparando envio de e-mails sobre aviso de lançamentos vencidos");
		}
		
		List<Lancamento> lancamentosVencidos = lancamentoRepository.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());
		
		if(lancamentosVencidos.isEmpty()) {
			logger.info("Sem lançamentos vencidos para enviar");
			
			return;
		}
		
		logger.info("Existem {} lançamentos vencidos." + lancamentosVencidos.size());
		
		List<Usuario> destinatarios = usuarioRepository.findByPermissoesDescricao(DESTINATARIOS);
		
		if(destinatarios.isEmpty()) {
			logger.warn("Existem lançamentos vencidos mas não foram encontrados destinatários.");
			
			return;
		}
		
		mailer.avisarSobreLancamentosVencidos(lancamentosVencidos, destinatarios);
		
		logger.info("Envio de e-mail concluído.");
	}
	
	public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
		List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(inicio, fim);
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("DT_INICIO", Date.valueOf(inicio));
		parametros.put("DT_FIM", Date.valueOf(fim));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		
		InputStream inputStream = this.getClass().getResourceAsStream("/relatorio/lancamentos-por-pessoa.jasper");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, new JRBeanCollectionDataSource(dados));
		
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
	
	public Lancamento salvar(Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.findById(lancamento.getPessoa().getId()).orElseThrow(() -> new PessoaInexistenteOuInativaException());
		
		if(pessoa.isInativo()) throw new PessoaInexistenteOuInativaException();
		
		return lancamentoRepository.save(lancamento);
	}
}
