package br.com.rcv.comercial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import br.com.rcv.comercial.config.property.ApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(ApiProperty.class)
public class ComercialApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComercialApiApplication.class, args);
	}
}
