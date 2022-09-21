package com.paygarde.mailing.services;

import com.paygarde.mailing.filesmanager.FileManagerInterface;
import com.paygarde.mailing.model.MailInfo;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@Service
public class EmailServiceImpl implements EmailServiceInterface {

    //@Autowired
    private final Configuration configuration;
    //@Autowired
    private final JavaMailSender javaMailSender;
    @Autowired
    private FileManagerInterface fileManager;

    @Value("${email.template}")
    private String templateFolder;

    @Value("${email.attachment.folder.path}")
    private String tempFolderPath;

    public EmailServiceImpl(Configuration configuration, JavaMailSender javaMailSender) {
        this.configuration = configuration;
        this.javaMailSender = javaMailSender;
    }

    String name, username, email, mailSubject, templateName;
    String filesUrlArray[];


    @Override
    public boolean sendEmail(MailInfo mailInfo) throws MessagingException, IOException, TemplateException {

        File tempFolder = fileManager.createFolder(tempFolderPath);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(mailInfo.getEmail());
        helper.setSubject(mailInfo.getMailSubject());
        StringWriter stringWriter = new StringWriter();
        configuration.setDirectoryForTemplateLoading(new File(
                templateFolder));
        configuration.getTemplate(mailInfo.getTemplateName()).process(mailInfo, stringWriter);
        String emailContent = stringWriter.getBuffer().toString();
        helper.setText(emailContent, true);

        boolean res=fileManager.attachFiles(mailInfo.getFilesUrl(), tempFolder, helper);
        if(res){
            javaMailSender.send(mimeMessage);
            fileManager.deleteFile(tempFolder);
            return true;

        }else {
            fileManager.deleteFile(tempFolder);
            return false;
        }


    }


}
