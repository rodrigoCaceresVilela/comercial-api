package br.com.rcv.comercial.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import br.com.rcv.comercial.config.property.ApiProperty;

@Configuration
public class MailConfig {

	@Autowired
	private ApiProperty apiProp;
	
	@Bean
	public JavaMailSender javaMailSender() {
		Properties prop = new Properties();
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", true);
		prop.put("mail.smtp.connectiontimeout", "10000");
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setJavaMailProperties(prop);
		mailSender.setHost(apiProp.getMail().getHost());
		mailSender.setPort(apiProp.getMail().getPort());
		mailSender.setUsername(apiProp.getMail().getUsername());
		mailSender.setPassword(apiProp.getMail().getPassword());
		
		return mailSender;
	}
}
