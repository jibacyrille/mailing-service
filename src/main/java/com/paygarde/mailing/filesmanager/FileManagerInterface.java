package com.paygarde.mailing.filesmanager;

import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import java.io.File;


public interface FileManagerInterface {

    public boolean deleteFile(File element);

    public File createFolder(String folderName);

    public File downloadFile(File tempFolder, String fileUrl);

    public boolean urlIsValid(String url);

    public double fileSize(File f);

    public boolean attachFiles(String []urls,File tempFolder,MimeMessageHelper helper) throws MessagingException;
}
