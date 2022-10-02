package com.paygarde.mailing.services;

import com.paygarde.mailing.exceptions.AttachmentMaxSizeExcededException;
import com.paygarde.mailing.exceptions.MalformedUrlException;
import com.paygarde.mailing.model.MailInfo;
import com.paygarde.mailing.results.Result;
import com.paygarde.mailing.results.ResultDto;
import freemarker.template.TemplateException;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Path;

public interface EmailServiceInterface {
    ResultDto sendEmail(MailInfo mailInfo) throws MessagingException, IOException, TemplateException;
    boolean attachFiles(String[] urls, Path tempFolder) throws MessagingException, AttachmentMaxSizeExcededException, MalformedUrlException;

}
