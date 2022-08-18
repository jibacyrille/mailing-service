package com.paygarde.mailing.services;

import com.paygarde.mailing.models.MailInfo;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailServiceInterface{

    @Autowired
    final Configuration configuration;
    @Autowired
    final JavaMailSender javaMailSender;

    public EmailServiceImpl(Configuration configuration, JavaMailSender javaMailSender) {
        this.configuration = configuration;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(MailInfo mailInfo) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        //helper.setSubject("Welcome To SpringHow.com(LE TUTO QUE TU AS ENVOYE)");
        helper.setTo(mailInfo.getUserInfo().getEmail());
        helper.setSubject(mailInfo.getMailSubject());
        String emailContent = getEmailContent(mailInfo);
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }

    @Override
    public String getEmailContent(MailInfo mailInfo) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("mailInfo", mailInfo);
        //configuration.getTemplate("email.ftlh").process(model, stringWriter);
        configuration.getTemplate(mailInfo.getMailTemplate().getTemplateName()).process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

}
