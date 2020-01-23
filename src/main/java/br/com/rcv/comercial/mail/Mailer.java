package br.com.rcv.comercial.mail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import br.com.rcv.comercial.model.Lancamento;
import br.com.rcv.comercial.repository.LancamentoRepository;

@Component
public class Mailer {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine thymeleaf;
	
	@Autowired
	private LancamentoRepository repo;
	
//	@EventListener
//	private void testeEnvioDeEmail(ApplicationReadyEvent event) {
//		System.out.println("Início do envio do e-mail:");
//		
//		this.enviarEmail("rodrigocaceresvilela@yahoo.com.br", 
//						 Arrays.asList("rodrigocaceresvilela@gmail.com"), 
//						 "Aula sobre envio de e-mail", 
//						 "<h1>Envio com sucesso!</h1>");
//		
//		System.out.println("Término do envio do e-mail:");
//	}
	
	@EventListener
	private void testeEnvioDeEmailDeLancamentos(ApplicationReadyEvent event) {
		String template = "mail/aviso-lancamentos-vencidos";
		
		List<Lancamento> lancamentos = repo.findAll();
		
		Map<String, Object> variaveis = new HashMap<>();
		variaveis.put("lancamentos", lancamentos);
		
		this.enviarEmail("rodrigocaceresvilela@yahoo.com.br", 
				 Arrays.asList("rodrigocaceresvilela@gmail.com"), 
				 "Aviso de lançamentos", template, variaveis);
	}
	
	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String template, Map<String, Object> variaveis) {
		Context context = new Context(new Locale("pt", "BR"));
		
		variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));
		
		String mensagem = thymeleaf.process(template, context);
		
		this.enviarEmail(remetente, destinatarios, assunto, mensagem);
	}
	
	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {
		MimeMessage mime = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mime, "UTF-8");
			helper.setFrom(remetente);
			helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			helper.setSubject(assunto);
			helper.setText(mensagem, true);
			
			mailSender.send(mime);

		} catch (MessagingException e) {
			throw new RuntimeException("Problemas no envio do e-mail. " + e);
		}
	}
}
