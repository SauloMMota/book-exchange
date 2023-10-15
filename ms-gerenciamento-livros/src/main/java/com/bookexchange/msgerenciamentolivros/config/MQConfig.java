package com.bookexchange.msgerenciamentolivros.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
	
	@Value("${mq.queues.envio-notificacoes}")
	private String envioNotificacoesFila;
	
	@Bean
	public Queue queueEmissaoCartoes() {
		return new Queue(envioNotificacoesFila, true);
	}
	
}
