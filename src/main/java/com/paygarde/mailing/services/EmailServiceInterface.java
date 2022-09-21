package com.paygarde.mailing.services;

import com.paygarde.mailing.model.MailInfo;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;
public interface EmailServiceInterface {
    boolean sendEmail(MailInfo mailInfo) throws MessagingException, IOException, TemplateException;

}
