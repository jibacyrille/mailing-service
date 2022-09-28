package com.paygarde.mailing.services;

import com.paygarde.mailing.model.MailInfo;
import com.paygarde.mailing.results.Result;
import com.paygarde.mailing.results.ResultDto;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;
public interface EmailServiceInterface {
    ResultDto sendEmail(MailInfo mailInfo) throws MessagingException, IOException, TemplateException;

}
