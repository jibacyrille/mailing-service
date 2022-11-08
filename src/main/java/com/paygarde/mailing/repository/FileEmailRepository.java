package com.paygarde.mailing.repository;

import com.paygarde.mailing.results.ResultDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Repository
public class FileEmailRepository implements EmailRepositoryInterface{

    @Value("${email.folder.path}")
    private Path emailRep;


    public void addSuccess(ResultDto emailResultDto) throws IOException {
        File file=new File(emailRep+"/email/"+buildFileName("_success.txt"));

        FileWriter writer;
        try{
            writer=new FileWriter(file,true);
            writer.write(emailResultDto.getUid()+" ; "+emailResultDto.getMessage()+"\n");
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("The email with UUID"+emailResultDto.getUid()+
                " sent to "+emailResultDto.getMailInfo().getEmail()+" has been added successfully.");
    }

    public void addErrors(ResultDto emailResultDto) throws IOException {
        File file=new File(emailRep+"/email/"+buildFileName("_errors.txt"));

        FileWriter writer;
        try{
            writer=new FileWriter(file,true);
            writer.write(emailResultDto.getUid()+" ; "+emailResultDto.getMessage()+"\n");
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("The email with UUID"+emailResultDto.getUid()+
                " failed to "+emailResultDto.getMailInfo().getEmail()+" has been added in the errors " +
                "file successfully.");
    }

    public Path getFile() {
        return emailRep;
    }

    public void setFile(Path emailRep) {
        this.emailRep = emailRep;
    }

    String buildFileName(String state){
        //String temp="_success.txt";
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");
        String s = SDF.format(date);
        return s+state;
    }

    //20220917_success.txt

}
