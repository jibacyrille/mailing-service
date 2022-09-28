package com.paygarde.mailing.services;

import com.paygarde.mailing.filesmanager.FileManagerInterface;
import com.paygarde.mailing.model.MailInfo;
import com.paygarde.mailing.results.MailStatus;
import com.paygarde.mailing.results.ResultDto;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
//import org.modelmapper.ModelMapper;
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
import java.nio.file.Path;
import java.util.Date;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailServiceInterface {

    //@Autowired
    private final Configuration configuration;
    //@Autowired
    private final JavaMailSender javaMailSender;
   // @Autowired
    private final FileManagerInterface fileManager;

    @Value("${email.template}")
    private String templateFolder;

    @Value("${email.attachment.folder.path}")
    private String tempFolderPath;

    public EmailServiceImpl(Configuration configuration, JavaMailSender javaMailSender, FileManagerInterface fileManager) {
        this.configuration = configuration;
        this.javaMailSender = javaMailSender;
        this.fileManager = fileManager;
    }

    String name, username, email, mailSubject, templateName;
    String filesUrlArray[];


    @Override
    public ResultDto sendEmail(MailInfo mailInfo) throws MessagingException, IOException, TemplateException {

        //ModelMapper modelMapper = new ModelMapper();
        //ResultDto resultDto=modelMapper.map(mailInfo, ResultDto.class);
        ResultDto resultDto=new ResultDto();
        resultDto.setMailInfo(mailInfo);

        String emailId = UUID.randomUUID().toString();
        Date emailCreationTime=new Date();
        resultDto.setUid(emailId);
        resultDto.setCreationDataTime(emailCreationTime);
        resultDto.setStatus(MailStatus.CREATED);

        Path tempFolder = fileManager.createFolder(tempFolderPath);
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
            fileManager.deleteFile(tempFolder.toFile());
            resultDto.setStatus(MailStatus.CLOSED);
            resultDto.setMessage("Message sent successfully");


        }else {
            fileManager.deleteFile(tempFolder.toFile());
            resultDto.setStatus(MailStatus.FAILED);
            resultDto.setMessage("Couldn't send the message");
        }

        return resultDto;
    }


}
