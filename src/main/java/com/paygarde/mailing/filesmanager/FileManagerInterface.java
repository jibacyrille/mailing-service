package com.paygarde.mailing.filesmanager;

import com.paygarde.mailing.exceptions.AttachmentMaxSizeExcededException;
import com.paygarde.mailing.exceptions.MalformedUrlException;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;


public interface FileManagerInterface {

    public boolean deleteFile(File element);

    public Path createTempFolder(String folderName) ;
    public Path createFolder(String folderName) ;

    public File downloadFile(File tempFolder, String fileUrl);

    public boolean urlIsValid(String url);

    public long fileSize(File f);

}
