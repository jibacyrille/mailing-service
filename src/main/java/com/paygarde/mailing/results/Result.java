package com.paygarde.mailing.results;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


public class Result {
    private Date creationDataTime;
    private MailStatus status;
    private String message;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

