package com.bookexchange.msnotificacoes.mqueue;

import com.bookexchange.msnotificacoes.domain.representation.DadosEnvioNotificacao;
import com.bookexchange.msnotificacoes.exception.InternalServerErrorException;
import com.bookexchange.msnotificacoes.service.EnvioEmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnvioNotificacaoSubscriber {

    private final EnvioEmailService envioEmailService;

    @RabbitListener(queues = "${mq.queues.envio-notificacoes}")
    public void receberSolicitacaoEmissao(@Payload String payload) {
        try {
            var mapper = new ObjectMapper();
            DadosEnvioNotificacao dados = mapper.readValue(payload, DadosEnvioNotificacao.class);
            log.info("Dados: ", dados);
            envioEmailService.enviarEmail(dados);
        } catch (RuntimeException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao consumir os dados.", e);
        } catch (JsonMappingException e) {
            throw new InternalServerErrorException("Ocorreu um erro ao realizar " +
                    "o mapeamento da repersentação dos dados.",e);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException("Ocorreu um erro no processamento dos dados.",e);
        }
    }
}
