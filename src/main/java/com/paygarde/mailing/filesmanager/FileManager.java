package com.paygarde.mailing.filesmanager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
@RestController
public class FileManager implements FileManagerInterface{

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
    public File createFolder(String folderName) {
        File dossierImage = new File(folderName);
        dossierImage.mkdir();
        return dossierImage;
    }

    @Override
    public File downloadFile(File tempFolder, String fileUrl) {
        //File dossierImage = createFolder(tempFolderPath);
        try {
            URL url = new URL(fileUrl);

            desFile = new File(tempFolder.getAbsolutePath() + "/" + FilenameUtils.getName(url.getPath()));
            FileUtils.copyURLToFile(url, desFile);
        } catch (MalformedURLException e) {
            e.printStackTrace();
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
        } catch (Exception ex) { }
        return false;
    }

    @Override
    public double fileSize(File f){
        int temp=1024*1024;
        return (f.length()*10/temp);
    }

    @Override
    public boolean attachFiles(String[] urls, File tempFolder, MimeMessageHelper helper) throws MessagingException {
        boolean state=true;
        int attachedNumber=urls.length;
        if (attachedNumber!=0){
            for (String url : urls
            ) {
                if(this.urlIsValid(url)){
                    File f=downloadFile(tempFolder, url);
                    double fileSize=this.fileSize(f);

                    if(fileSize<=Double.parseDouble(fileMaxSize)){
                        helper.addAttachment(f.getName(), f);
                    } else{
                        System.out.println("The size of the file "+f.getName()+" exceeds the allowed size");
                        state=false;
                    }

                } else {
                    System.out.println("The Url: "+url+" is not valid");
                    state=false;
                }

            }
        }
        return state;
    }

}
