package br.com.rcv.comercial.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("comercialapi")
public class ApiProperty {

	private final Seguranca seguranca = new Seguranca();
	private final CorsOrigin corsOriginAllowed = new CorsOrigin();

	public Seguranca getSeguranca() {
		return seguranca;
	}
	
	public CorsOrigin getCorsOrigin() {
		return corsOriginAllowed;
	}
	
	public static class Seguranca {
		
		private boolean enableHttps;

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}
	}
	
	public static class CorsOrigin {
		
		private String originPermitida = "http://localhost:8000";

		public String getOriginPermitida() {
			return originPermitida;
		}

		public void setOriginPermitida(String originPermitida) {
			this.originPermitida = originPermitida;
		}
	}
}
