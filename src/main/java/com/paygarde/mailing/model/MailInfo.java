package com.paygarde.mailing.model;

import lombok.Data;

@Data
public class MailInfo {
    String name;
    String username;
    String email;
    String mailSubject;
    String templateName;
    String []filesUrl;
}
