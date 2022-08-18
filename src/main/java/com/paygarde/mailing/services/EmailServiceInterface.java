package com.paygarde.mailing.services;

import com.paygarde.mailing.models.MailInfo;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailServiceInterface {
    void sendEmail(MailInfo mailInfo) throws MessagingException, IOException, TemplateException;
    String getEmailContent(MailInfo mailInfo) throws IOException, TemplateException ;

}
