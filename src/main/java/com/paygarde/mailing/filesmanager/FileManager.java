package com.paygarde.mailing.filesmanager;

import com.paygarde.mailing.exceptions.AttachmentMaxSizeExcededException;
import com.paygarde.mailing.exceptions.MalformedUrlException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileManager implements FileManagerInterface{

     private final Logger log = LoggerFactory.getLogger(this.getClass());

     boolean state;
    private File desFile;
    @Value("${email.attachment.max.size}")
    private String fileMaxSize;

    @Override
    public boolean deleteFile(File element) {
        if (element.isDirectory()) {
            for (File sub : element.listFiles()) {
                deleteFile(sub);
            }
        }
        return element.delete();
    }

    @Override
    public Path createFolder(String folderName)  {
        /*File dossierImage = new File(folderName);
        dossierImage.mkdir();
        return dossierImage;*/
        try {
            return Files.createTempDirectory(folderName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public File downloadFile(File tempFolder, String fileUrl) {
        //File dossierImage = createFolder(tempFolderPath);
        try {
            URL url = new URL(fileUrl);

            desFile = new File(tempFolder.getAbsolutePath() + "/" + FilenameUtils.getName(url.getPath()));

            FileUtils.copyURLToFile(url, desFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return desFile;
    }

        @Override
    public boolean urlIsValid(String url) {
        try {
            (new java.net.URL(url)).openStream().close();
            return true;
        } catch (Exception ex) { return false;}

    }

    @Override
    public long fileSize(File f){
        int temp=1024*1024;
        long s=f.length();
        return (s*10/temp);
    }

    @Override
    public boolean attachFiles(String[] urls, Path tempFolder, MimeMessageHelper helper) throws MessagingException, AttachmentMaxSizeExcededException, MalformedURLException {
        state=true;
        int attachedNumber=urls.length;
        if (attachedNumber!=0){
            for (String url : urls
            ) {
                if(this.urlIsValid(url)){
                    File f=downloadFile(tempFolder.toFile(), url);
                    Long fileSize=this.fileSize(f);

                    if(fileSize<=Long.parseLong(fileMaxSize)){
                        helper.addAttachment(f.getName(), f);
                    } else{
                        log.error("The size of the file "+f.getName()+" exceeds the allowed size");
                        state=false;
                      //  throw new AttachmentMaxSizeExcededException("The size of the file "+f.getName()+" exceeds the allowed size");

                    }

                } else {

                    log.error("The Url: "+url+" is not valid");
                    state=false;
                    //throw new MalformedURLException("The Url: "+url+" is not valid");

                }

            }
        }

        return state;
    }

}