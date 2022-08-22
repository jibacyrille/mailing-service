package com.paygarde.mailing.controller;

import com.paygarde.mailing.models.MailInfo;
import com.paygarde.mailing.services.EmailServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailController {

    @Autowired
    private EmailServiceInterface emailService;

    public EmailServiceInterface getEmailService() {
        return emailService;
    }

    public void setEmailServiceInterface(EmailServiceInterface emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendemail")
    @ResponseBody
    public String sendEmail(@RequestBody MailInfo mailInfo) throws Exception {
        emailService.sendEmail(mailInfo);

        System.out.println("la methode sendEmail du controller a ete invoquee");
        return "message-sent";
    }

}