package com.paygarde.mailing.services;

import com.paygarde.mailing.model.MailInfo;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

public interface EmailServiceInterface {
    void sendEmail(MailInfo mailInfo) throws MessagingException, IOException, TemplateException;

}
