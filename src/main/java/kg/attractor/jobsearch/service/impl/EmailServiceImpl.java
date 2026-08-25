package kg.attractor.jobsearch.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kg.attractor.jobsearch.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Override
    public void sendResetPasswordEmail(String toEmail, String resetLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromEmail, "JobSearch Support");
        helper.setTo(toEmail);
        helper.setSubject("Восстановление пароля JobSearch");

        String content = "<p>Здравствуйте!</p>"
                + "<p>Вы запросили восстановление пароля для вашего аккаунта JobSearch.</p>"
                + "<p>Перейдите по ссылке, чтобы задать новый пароль:</p>"
                + "<p><a href=\"" + resetLink + "\">Восстановить пароль</a></p>"
                + "<p>Ссылка одноразовая. Если вы не запрашивали восстановление пароля, "
                + "просто проигнорируйте это письмо — пароль останется прежним.</p>";

        helper.setText(content, true);
        mailSender.send(message);
        log.info("Письмо для восстановления пароля отправлено на адрес {}", toEmail);
    }
}
