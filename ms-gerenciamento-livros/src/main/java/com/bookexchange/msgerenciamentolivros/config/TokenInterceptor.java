package com.bookexchange.msgerenciamentolivros.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.Jwt;

public class TokenInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // Obtém o token do contexto de segurança
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(jwt != null) {
            // Adiciona o token ao cabeçalho da solicitação
            requestTemplate.header("Authorization", "Bearer " + jwt.getTokenValue());
        }
    }

}
