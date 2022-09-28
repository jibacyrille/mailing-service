package com.paygarde.mailing.exceptions;

import java.io.IOException;

public class MalformedUrlException extends IOException {
    public MalformedUrlException(String message) {
        super(message);
    }
}
