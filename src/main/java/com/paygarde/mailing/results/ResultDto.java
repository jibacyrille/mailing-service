package com.paygarde.mailing.results;

import com.paygarde.mailing.model.MailInfo;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;



public class ResultDto implements Serializable {
    private Date creationDataTime;
    private MailStatus status;
    private String message;
    private MailInfo mailInfo;
    private String uid;

    public Date getCreationDataTime() {
        return creationDataTime;
    }

    public void setCreationDataTime(Date creationDataTime) {
        this.creationDataTime = creationDataTime;
    }

    public MailStatus getStatus() {
        return status;
    }

    public void setStatus(MailStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MailInfo getMailInfo() {
        return mailInfo;
    }

    public void setMailInfo(MailInfo mailInfo) {
        this.mailInfo = mailInfo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

