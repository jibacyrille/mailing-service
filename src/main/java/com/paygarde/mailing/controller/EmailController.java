package com.paygarde.mailing.controller;

import com.paygarde.mailing.model.MailInfo;
import com.paygarde.mailing.results.*;
import com.paygarde.mailing.services.EmailServiceInterface;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
public class EmailController {

    private static final Logger LOGGER =  LogManager.getLogger( EmailController.class );

    @Autowired
    private EmailServiceInterface emailService;
    //Map<String, Object> model = new HashMap<>();


    @PostMapping("/sendemail")
    public ResponseEntity<Result> sendEmail(@RequestBody MailInfo mailInfo) throws Exception {
        ResultDto resultDto=emailService.sendEmail(mailInfo);

        System.out.println(resultDto.getMailInfo().getMailSubject());
        System.out.println(Arrays.toString(resultDto.getMailInfo().getFilesUrl()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        Result result=ResultMapper.INSTANCE.resultDtoToResult(resultDto);
        System.out.println(result.getMessage());


        if(result.getStatus().equals(MailStatus.CLOSED)){

            emailService.saveSentEmail(resultDto);
            return new ResponseEntity<Result>(result, headers,HttpStatus.OK);

        }else {
            emailService.saveFailedEmail(resultDto);
            return new ResponseEntity<Result>(result, headers, HttpStatus.BAD_REQUEST);
        }

    }


}