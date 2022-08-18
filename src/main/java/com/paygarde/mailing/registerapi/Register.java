package com.paygarde.mailing.registerapi;

import com.paygarde.mailing.models.MailInfo;
import com.paygarde.mailing.services.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/register")
public class Register {
    @Autowired
    private EmailServiceImpl emailService;

    public EmailServiceImpl getEmailService() {
        return emailService;
    }

    public void setEmailService(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }

    @RequestMapping(method = RequestMethod.POST,path = "/register")
    @ResponseBody

    public String register(@RequestBody MailInfo mailInfo) throws Exception {
        emailService.sendEmail(mailInfo);
        return "Email Sent..!";
    }
}
