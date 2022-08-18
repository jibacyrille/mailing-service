package com.paygarde.mailing.controller;

import com.paygarde.mailing.models.MailInfo;
import com.paygarde.mailing.services.EmailServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmailController {

    @Autowired
    private EmailServiceInterface emailService;

    public EmailServiceInterface getEmailService() {
        return emailService;
    }

    public void setEmailServiceInterface(EmailServiceInterface emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/register")
    @ResponseBody
    public String register(@RequestBody MailInfo mailInfo) throws Exception {
        emailService.sendEmail(mailInfo);

        System.out.println("la methode register a ete invoque");
        return "message-sent";
    }

    @PostMapping("/create")
    public String createMailInfo(@ModelAttribute("form") MailInfo mailInfo) throws Exception {
        emailService.sendEmail(mailInfo);

        System.out.println("la methode createUseForm a ete invoque donc le message a ete envoye");
        return "message-sent";
    }

    @GetMapping("/home")
    public String displayHome(){
        System.out.println("La methode displayHome a été invoquée");
        return "index";
    }

    @RequestMapping("/create-form")
    public String displayEmailForm(@ModelAttribute("form") MailInfo mailInfo){
        System.out.println("La methode displayEmailForm a été invoquée");
        return "emailform";
    }
}