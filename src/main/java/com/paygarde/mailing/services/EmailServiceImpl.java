package com.paygarde.mailing.services;

import com.paygarde.mailing.model.MailInfo;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.thymeleaf.util.ArrayUtils.toArray;

@Service
public class EmailServiceImpl implements EmailServiceInterface{

    //@Autowired
    private final Configuration configuration;
    //@Autowired
    private final JavaMailSender javaMailSender;

    @Value("${email.template}")
    private String templateFolder;

    @Value("${email.attachment.folder.path}")
    private String tempFolderPath;

    public EmailServiceImpl(Configuration configuration, JavaMailSender javaMailSender) {
        this.configuration = configuration;
        this.javaMailSender = javaMailSender;
    }

    String name,username,email,mailSubject,templateName;
    String filesUrl[];

    File desFile;
    URL url;

    @Override
    public void sendEmail(MailInfo mailInfo) throws MessagingException, IOException, TemplateException {

        name=mailInfo.getName();
        username=mailInfo.getUsername();
        email=mailInfo.getEmail();
        mailSubject=mailInfo.getMailSubject();
        templateName=mailInfo.getTemplateName();
        filesUrl= mailInfo.getFilesUrl();

        System.out.println(name);
        System.out.println(username);
        System.out.println(email);
        System.out.println(mailSubject);
        System.out.println(templateName);
        System.out.println(filesUrl[0]);
        System.out.println(Arrays.toString(filesUrl));
        for (String s:filesUrl
             ) {System.out.println(s);

        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        //MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        //MimeMessage message = mailSender.createMimeMessage();
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
        //helper.setText("<html><body><img src='cid:hello123'></body></html>");



        //UtilMailWithAttach se = new UtilMailWithAttach(true);

        File dossierImage = new File("d://images/");
        dossierImage.mkdir();
        System.out.println(dossierImage.getAbsolutePath());
        try {
                     url = new URL("https://randomuser.me/api/portraits/women/50.jpg");
                     String extension= FilenameUtils.getExtension(url.getPath());
                     System.out.println(extension);

                     desFile=new File(dossierImage.getAbsolutePath()+"/"+FilenameUtils.getName(url.getPath()));
                     FileUtils.copyURLToFile(url, desFile);
                     if(desFile.exists()){
                         helper.addAttachment(desFile.getName(),desFile);

                     }
                  } catch (MalformedURLException e) {
                     e.printStackTrace();
                  } catch (IOException e) {
                     e.printStackTrace();
                  }

        try {
            url = new URL("https://ww2.ac-poitiers.fr/ecogest/IMG/pdf/bac_pro_vente_developpement_personnel.pdf");
            String extension= FilenameUtils.getExtension(url.getPath());
            System.out.println(extension);

            desFile=new File(dossierImage.getAbsolutePath()+"/"+FilenameUtils.getName(url.getPath()));
            FileUtils.copyURLToFile(url, desFile);
            if(desFile.exists()){
                helper.addAttachment(desFile.getName(),desFile);

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        ClassPathResource pdf = new ClassPathResource("static/attachment.pdf");
        ClassPathResource image = new ClassPathResource("static/hello.jpg");

        FileSystemResource file = new FileSystemResource(new File("D:/hello.jpg"));
        helper.addAttachment(file.getFilename(),file);

        helper.addAttachment("attachment.pdf", pdf);

        helper.addInline("hello123", image);
       javaMailSender.send(mimeMessage);


        boolean result = deleteFile(dossierImage);
        if (result) {
            System.out.println(desFile.getName() + " is deleted!");
        } else {
            System.out.println("Sorry, unable to delete the file.");
        }

    }

    public boolean deleteFile(File element) {
        if (element.isDirectory()) {
            for (File sub : element.listFiles()) {
                deleteFile(sub);
            }
        }
        return element.delete();
    }

    public File createAttachmentFolder(){
        File dossierImage = new File(tempFolderPath);
        dossierImage.mkdir();
       return dossierImage;
    }
}
