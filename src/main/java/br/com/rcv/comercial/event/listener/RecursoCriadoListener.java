package br.com.rcv.comercial.event.listener;

import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.rcv.comercial.event.RecursoCriadoEvent;

@Component
public class RecursoCriadoListener {//implements ApplicationListener<RecursoCriadoEvent> {

	//@Override
	@EventListener
	public void onApplicationEvent(RecursoCriadoEvent event) {
		HttpServletResponse response = event.getResponse();
		
		response.addHeader("Teste_Header", "Valor do Header");
	}
}
