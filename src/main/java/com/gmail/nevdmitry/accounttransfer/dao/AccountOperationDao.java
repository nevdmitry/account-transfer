package com.gmail.nevdmitry.accounttransfer.dao;

import java.util.Collection;
import com.gmail.nevdmitry.accounttransfer.domain.Transaction;
import com.gmail.nevdmitry.accounttransfer.exception.AccountOperationException;

public interface AccountOperationDao {

    /**
     * Transfers money from source account to the target one.
     *
     * @param sourceAccId source account id
     * @param targetAccId target account if
     * @param amount      amount in big decimal (min ETH doesn't fit to the double)
     *
     * @throws AccountOperationException if error occurs during money transfer
     */
    void transfer(long sourceAccId, long targetAccId, double amount) throws AccountOperationException;
    
    double accountBalance(long accountId);

    Collection<Transaction> transactions();
}
