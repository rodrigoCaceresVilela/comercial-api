package br.com.rcv.comercial.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.rcv.comercial.controller.ComercialResponseExceptionHandler.Erro;
import br.com.rcv.comercial.dto.LancamentoEstatisticaCategoria;
import br.com.rcv.comercial.dto.LancamentoEstatisticaDia;
import br.com.rcv.comercial.model.Lancamento;
import br.com.rcv.comercial.repository.LancamentoRepository;
import br.com.rcv.comercial.repository.filter.LancamentoFilter;
import br.com.rcv.comercial.repository.projection.ResumoLancamento;
import br.com.rcv.comercial.service.LancamentoService;
import br.com.rcv.comercial.service.exception.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	/**
	 * 
	 * Endpoint para receber arquivo e gravar na pasta do servidor
	 * 
	 * @param anexo
	 * @return HttpStatus
	 * @throws IOException
	 */
	@PostMapping("/anexo")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public HttpStatus uploadAnexo(@RequestParam MultipartFile anexo) throws IOException {
		OutputStream out = new FileOutputStream("C:/Users/rodrigo.caceres/Desktop/anexo--" + anexo.getOriginalFilename());
		
		out.write(anexo.getBytes());
		out.close();
		
		return HttpStatus.OK;
	}
	
	@GetMapping("/estatisticas/por-categoria")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<LancamentoEstatisticaCategoria> porCategoria() {
		return lancamentoRepository.porCategoria(LocalDate.now());
	}
	
	@GetMapping("/estatisticas/por-dia")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<LancamentoEstatisticaDia> porDia() {
		return lancamentoRepository.porDia(LocalDate.now());
	}
	
	@GetMapping
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.filtrar(lancamentoFilter, pageable);
	}
	
	@GetMapping(params = "resumo")
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.resumir(lancamentoFilter, pageable);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Lancamento> findById(@PathVariable Long id) {
		Optional<Lancamento> lancamentoSalvo = lancamentoRepository.findById(id);
		
		return lancamentoSalvo.isPresent() ? ResponseEntity.ok().body(lancamentoSalvo.get()) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<Lancamento> create(@Valid @RequestBody Lancamento lancamento) {
		Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(lancamentoSalvo.getId()).toUri();
		
		return ResponseEntity.created(uri).body(lancamentoSalvo);
	}
	
	@PutMapping("/{id}")
	public Lancamento update(@PathVariable Long id, @RequestBody Lancamento lancamento) {
		Lancamento lancamentoAtual = lancamentoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lancamento não encontrado."));
		
		BeanUtils.copyProperties(lancamento, lancamentoAtual, "id");
		
		return lancamentoRepository.saveAndFlush(lancamentoAtual);
	}
	
	@GetMapping("/relatorios/por-pessoa")
	public ResponseEntity<byte[]> relatorioPorPessoa(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim) throws Exception {
		
		byte[] relatorio = lancamentoService.relatorioPorPessoa(inicio, fim);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(relatorio);
	}
	
	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		
		return ResponseEntity.badRequest().body(erros);
	}
	
}
