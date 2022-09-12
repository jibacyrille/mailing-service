package com.paygarde.mailing.services;

import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

public interface EmailServiceInterface {
    void sendEmail(Map<String, Object> mailInfo) throws MessagingException, IOException, TemplateException;

}
