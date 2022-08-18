package com.paygarde.mailing.models;

import lombok.Data;

@Data
public class MailInfo {
    private UserInfo userInfo;
    private String mailSubject;
    private Template mailTemplate;

}
