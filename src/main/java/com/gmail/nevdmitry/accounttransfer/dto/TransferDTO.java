package com.gmail.nevdmitry.accounttransfer.dto;

import lombok.*;

@Value
public class TransferDTO {
    long sourceAccId;
    long targetAccId;
    double amount; //should be BigDecimal in case of small amount (min ETH doesn't fit to the double)
}
