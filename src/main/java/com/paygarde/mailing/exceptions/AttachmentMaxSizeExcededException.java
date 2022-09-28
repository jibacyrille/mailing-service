package com.paygarde.mailing.exceptions;

import java.io.IOException;

public class AttachmentMaxSizeExcededException extends IOException {
    public AttachmentMaxSizeExcededException(String message) {
        super(message);
    }
}
