package com.paygarde.mailing.services;

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

    //@Autowired
    private final Configuration configuration;
    //@Autowired
    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(Configuration configuration, JavaMailSender javaMailSender) {
        this.configuration = configuration;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(Map<String, Object>mailInfo) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo(mailInfo.get("email").toString());
        helper.setSubject(mailInfo.get("mailSubject").toString());
        StringWriter stringWriter = new StringWriter();
        configuration.getTemplate(mailInfo.get("templateName").toString()).process(mailInfo, stringWriter);
        String emailContent = stringWriter.getBuffer().toString();
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }
}
