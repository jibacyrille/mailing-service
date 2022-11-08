package com.paygarde.mailing.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paygarde.mailing.exceptions.AttachmentMaxSizeExcededException;
import com.paygarde.mailing.exceptions.MalformedUrlException;
import com.paygarde.mailing.filesmanager.FileManagerInterface;
import com.paygarde.mailing.model.MailInfo;
import com.paygarde.mailing.repository.FileEmailRepository;
import com.paygarde.mailing.repository.EmailRepositoryInterface;
import com.paygarde.mailing.results.MailStatus;
import com.paygarde.mailing.results.ResultDto;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
//import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailServiceInterface {

    //@Autowired
    private final Configuration configuration;
    //@Autowired
    private final JavaMailSender javaMailSender;
   // @Autowired
    private final FileManagerInterface fileManager;

    @Autowired
    private EmailRepositoryInterface emailRepository;

    public EmailServiceImpl(Configuration configuration, JavaMailSender javaMailSender, FileManagerInterface fileManager, EmailRepositoryInterface emailRepository) {
        this.configuration = configuration;
        this.javaMailSender = javaMailSender;
        this.fileManager = fileManager;
        this.emailRepository=emailRepository;
    }

    boolean state;

    public MimeMessageHelper helper;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${email.template}")
    private String templateFolder;

    @Value("${email.attachment.folder.path}")
    private String tempFolderPath;

    @Value("${email.attachment.max.size}")
    private String fileMaxSize;

    @Value("${email.folder.path}")
    private String emailRep;

    private File templateFolderFile;

    @Override
    public ResultDto sendEmail(MailInfo mailInfo) throws MessagingException, IOException, TemplateException {

        ResultDto resultDto=new ResultDto();
        resultDto.setMailInfo(mailInfo);

        String emailId = UUID.randomUUID().toString();
        Date emailCreationTime=new Date();
        resultDto.setUid(emailId);
        resultDto.setCreationDataTime(emailCreationTime);
        resultDto.setStatus(MailStatus.CREATED);
        resultDto.setMessage("Message created, awaiting to be sent");


        if(!priorityIsValid(mailInfo.getMailPriority())) {
            resultDto.setMessage("Couldn't send message, Invalid Priority ");
            resultDto.setStatus(MailStatus.FAILED);
            return  resultDto;
        } else{

        jsonMailSave(resultDto);

        Path tempFolder = fileManager.createTempFolder(tempFolderPath);
       // Path emailFolder= fileManager.createFolder(emailRep);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(mailInfo.getEmail());
        helper.setSubject(mailInfo.getMailSubject());
        StringWriter stringWriter = new StringWriter();
        templateFolderFile=new File(
                templateFolder);

        configuration.setDirectoryForTemplateLoading(templateFolderFile);
        configuration.getTemplate(mailInfo.getTemplateName()).process(mailInfo, stringWriter);
        String emailContent = stringWriter.getBuffer().toString();
        helper.setText(emailContent, true);



        try{
            attachFiles(mailInfo.getFilesUrl(), tempFolder);
            javaMailSender.send(mimeMessage);
            //fileManager.deleteFile(tempFolder.toFile());
            resultDto.setStatus(MailStatus.CLOSED);
            resultDto.setMessage("Message sent successfully");


        }catch (MessagingException | AttachmentMaxSizeExcededException | MalformedUrlException e){
            log.error(e.toString());
            //fileManager.deleteFile(tempFolder.toFile());
            resultDto.setStatus(MailStatus.FAILED);
            resultDto.setMessage("Couldn't send the message");
        }finally {
            fileManager.deleteFile(tempFolder.toFile());
            return resultDto;
        }
        }

    }

    @Override
    public void saveSentEmail(ResultDto resultDto) throws IOException {
        emailRepository.addSuccess(resultDto);

    }

    @Override
    public void saveFailedEmail(ResultDto resultDto) throws IOException {
        emailRepository.addErrors(resultDto);

    }


    public boolean attachFiles(String[] urls, Path tempFolder) throws MessagingException, AttachmentMaxSizeExcededException, MalformedUrlException {
        state=false;
        int attachedNumber=urls.length;
        if (attachedNumber!=0){
            for (String url : urls
            ) {
                if(fileManager.urlIsValid(url)){
                    File f=fileManager.downloadFile(tempFolder.toFile(), url);
                    Long fileSize=fileManager.fileSize(f);

                    if(fileSize<=Long.parseLong(fileMaxSize)){
                        helper.addAttachment(f.getName(), f);
                        state=true;
                    } else{
                        //log.error("The size of the file "+f.getName()+" exceeds the allowed size");
                        state=false;
                        throw new AttachmentMaxSizeExcededException("The size of the file "+f.getName()+" exceeds the allowed size");

                    }

                } else {

                    //log.error("The Url: "+url+" is not valid");
                    state=false;
                    throw new MalformedUrlException("The Url: "+url+" is not valid");

                }

            }
        }

        return state;
    }

    @Override
    public boolean priorityIsValid(String priority) {
        String s=priority.toLowerCase();
        return (s.equals("low")||s.equals("medium")||s.equals("height"));
    }

    public void jsonMailSave(ResultDto resultDto){
        String priority=resultDto.getMailInfo().getMailPriority().toLowerCase();

            System.out.println("_____________________________________________________________________");
            System.out.println(priority);
            System.out.println("_____________________________________________________________________");
            File file = new File(emailRep + "/email/" + priority + "/" + resultDto.getUid() + "_" + priority + ".json");
            String path = file.getPath();
            System.out.println(path);

            ObjectMapper Obj = new ObjectMapper();
            try {
                // Converting the Java object into a JSON string
                String jsonStr = Obj.writeValueAsString(resultDto);
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(jsonStr);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }


}


