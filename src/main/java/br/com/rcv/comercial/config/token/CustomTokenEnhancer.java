package br.com.rcv.comercial.config.token;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import br.com.rcv.comercial.security.UsuarioSistema;

public class CustomTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		UsuarioSistema usuarioSistema = (UsuarioSistema) authentication.getPrincipal();
		
		// Poderia colocar o nome do usuário mas não criei esse atributo na tabela. Esse atributo no token será usado para apresentar o nome do usuário na tela
		Map<String, Object> addInfo = new HashMap<>();
		addInfo.put("id", usuarioSistema.getUsuario().getId());
		addInfo.put("nome", usuarioSistema.getUsuario().getEmail());
		
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(addInfo);
		
		return accessToken;
	}
}
