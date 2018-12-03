package com.gmail.nevdmitry.accounttransfer.domain;

import lombok.*;

import java.util.Date;

@Value
public class Transaction {
    long transactionId;
    Date date;
    long sourceAccId;
    long targetAccId;
    double amount;
}
