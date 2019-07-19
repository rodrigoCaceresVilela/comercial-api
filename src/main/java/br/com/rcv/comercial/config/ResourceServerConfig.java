package br.com.rcv.comercial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

@Profile("oauth-security")
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/categorias/**").access("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
				.antMatchers(HttpMethod.POST, "/categorias/**").access("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
				.antMatchers(HttpMethod.PUT, "/categorias/**").access("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('update')")
				.antMatchers(HttpMethod.DELETE, "/categorias/**").access("hasAuthority('ROLE_REMOVER_CATEGORIA') and #oauth2.hasScope('delete')")
				.antMatchers(HttpMethod.GET, "/pessoas/**").access("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
				.antMatchers(HttpMethod.POST, "/pessoas/**").access("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
				.antMatchers(HttpMethod.PUT, "/pessoas/**").access("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('update')")
				.antMatchers(HttpMethod.DELETE, "/pessoas/**").access("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('delete')")
				.antMatchers(HttpMethod.GET, "/lancamentos/**").access("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
				.antMatchers(HttpMethod.POST, "/lancamentos/**").access("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
				.antMatchers(HttpMethod.PUT, "/lancamentos/**").access("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('update')")
				.antMatchers(HttpMethod.DELETE, "/lancamentos/**").access("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('delete')")
				.anyRequest().authenticated()
			.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.csrf().disable();
	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.stateless(true);
	}
	
	@Bean
	public MethodSecurityExpressionHandler createExpressionHandler() {
		return new OAuth2MethodSecurityExpressionHandler();
	}
}