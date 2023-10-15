package com.bookexchange.msgerenciamentolivros.mqueue;


import com.bookexchange.msgerenciamentolivros.domain.representation.DadosEnvioNotificacao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnvioNotificacaoPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueEnvioNotificacoes;

    public void enviarDadosNotificacao(DadosEnvioNotificacao dadosEnvioNotificacao) throws JsonProcessingException {
        var json = convertIntoJson(dadosEnvioNotificacao);
        rabbitTemplate.convertAndSend(queueEnvioNotificacoes.getName() ,json);
    }

    private String convertIntoJson(DadosEnvioNotificacao dadosEnvioNotificacao) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(dadosEnvioNotificacao);
        return json;
    }
}
