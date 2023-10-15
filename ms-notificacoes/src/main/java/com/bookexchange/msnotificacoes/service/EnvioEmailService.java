package com.bookexchange.msnotificacoes.service;

import com.bookexchange.msnotificacoes.domain.representation.DadosEnvioNotificacao;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnvioEmailService {
    private final static String ASSUNTO = "Nova Solicitação de Troca de Livros no Book Exchange";
    private final JavaMailSender javaMailSender;
    public void enviarEmail(DadosEnvioNotificacao dados) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(dados.getUsuarioReceptor().getEmail().toLowerCase().trim());
        message.setSubject(ASSUNTO);
        message.setText(montarCorpo(dados));
        javaMailSender.send(message);
    }

    private String montarCorpo(DadosEnvioNotificacao dados) {
        StringBuilder sb = new StringBuilder();
        sb.append("Olá, ").append(dados.getUsuarioReceptor().getNome()).append("\n")
                .append("Esperamos que você esteja bem! Temos uma ótima notícia para você em relação à sua " +
                        "paixão por livros e à sua participação ativa na nossa comunidade do \"Book Exchange\".\n")
                .append("Recentemente, um dos membros do nosso aplicativo,")
                .append(dados.getUsuarioEmissor().getNome())
                .append(", expressou interesse em realizar uma troca de livros com você." +
                        " Abaixo estão os detalhes dessa empolgante oportunidade de compartilhar leituras:\n")
                .append("Usuário Emissor: ").append(dados.getUsuarioEmissor().getNome()).append("\n")
                .append("Livros Envolvidos: \n")
                .append("Livro do usuário ").append(dados.getUsuarioEmissor().getNome()).append(":\n")
                .append("- Título: ").append(dados.getLivroEmissor().getTitulo()).append("\n")
                .append("- Autor: ").append(dados.getLivroEmissor().getAutor())
                .append("Seu Livro").append(dados.getUsuarioReceptor().getNome()).append(":\n")
                .append("- Título: ").append(dados.getLivroReceptor().getTitulo()).append("\n")
                .append("- Autor: ").append(dados.getLivroReceptor().getAutor()).append("\n")
                .append("Esta é uma grande chance de expandir sua biblioteca pessoal e conhecer novas histórias incríveis.\n" +
                        " Se você estiver interessado nesta troca, entre em contato com ")
                .append(dados.getUsuarioEmissor().getNome())
                .append("por meio do nosso aplicativo \"Book Exchange\" e discuta os detalhes da troca, como prazos e locais de entrega. \n")
                .append("Lembrando que a segurança é nossa prioridade. Certifique-se de fazer a troca em um local público e seguro. \n")
                .append("Se você tiver alguma dúvida ou precisar de assistência, não hesite em nos contatar. Estamos aqui para ajudar! \n")
                .append("Aproveite sua experiência de leitura no \"Book Exchange\" e continue compartilhando o amor pelos livros. \n")
                .append("Atenciosamente,\n")
                .append("A Equipe do Book Exchange");
        return sb.toString();
    }
}
