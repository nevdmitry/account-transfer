package com.gmail.nevdmitry.accounttransfer.exception;


import lombok.Getter;

public class AccountOperationException extends Exception {
    @Getter
    private final ErrorCode code;

    public AccountOperationException(ErrorCode code) {
        super();
        this.code = code;
    }

    public AccountOperationException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
