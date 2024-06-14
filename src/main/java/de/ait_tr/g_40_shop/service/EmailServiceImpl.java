package de.ait_tr.g_40_shop.service;

import de.ait_tr.g_40_shop.domain.entity.User;
import de.ait_tr.g_40_shop.service.Interfaces.ConfirmationService;
import de.ait_tr.g_40_shop.service.Interfaces.EmailService;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender sender;
    private final Configuration mailConfig;
    private final ConfirmationService confirmationService;

    public EmailServiceImpl(JavaMailSender sender, Configuration mailConfig, ConfirmationService confirmationService) {
        this.sender = sender;
        this.mailConfig = mailConfig;
        this.confirmationService = confirmationService;

        mailConfig.setDefaultEncoding("UTF-8");
        mailConfig.setTemplateLoader(new ClassTemplateLoader(EmailServiceImpl.class, "/mail/"));
    }


    // Отправка письма
    @Override
    public void sendConfirmationEmail(User user) {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        String text = generateConfirmationMail(user);

        try {
            helper.setFrom("d83935353@gmail.com");
            helper.setTo(user.getEmail());
            helper.setSubject("Registration confirmation");
            helper.setText(text, true);
            sender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    // Генерация письма
    private String generateConfirmationMail(User user){

        try {
            Template template = mailConfig.getTemplate("confirm_mail.ftlh");
            String code = confirmationService.generateConfirmationCode(user);

            // GET -> http://localhost:8080/register?code=${code}

            String url = "http://localhost:8080/register?code=" + code;

            Map<String, Object> templateMap = new HashMap<>();
            templateMap.put("name", user.getUsername());
            templateMap.put("link", url);

            return FreeMarkerTemplateUtils.processTemplateIntoString(template,templateMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
