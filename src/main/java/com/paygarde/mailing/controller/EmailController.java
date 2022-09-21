package com.paygarde.mailing.controller;

import com.paygarde.mailing.model.MailInfo;
import com.paygarde.mailing.services.EmailServiceInterface;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EmailController {

    private static final Logger LOGGER =  LogManager.getLogger( EmailController.class );


    @Autowired
    private EmailServiceInterface emailService;
    Map<String, Object> model = new HashMap<>();
    @PostMapping("/sendemail")
    //@ResponseBody
    public String sendEmail(@RequestBody MailInfo mailInfo) throws Exception {
        Boolean res=emailService.sendEmail(mailInfo);
        LOGGER.log( Level.INFO, "la methode sendEmail du controller a ete invoquee" );
        if(res){
            return "message sent successfully";

        }else {
            return "couldn't send the message. Some attached files have not been loaded";
        }

    }

}