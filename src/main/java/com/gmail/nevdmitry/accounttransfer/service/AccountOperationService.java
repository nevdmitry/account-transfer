package com.gmail.nevdmitry.accounttransfer.service;

import java.util.Collection;
import com.gmail.nevdmitry.accounttransfer.dao.AccountOperationDao;
import com.gmail.nevdmitry.accounttransfer.domain.Transaction;
import com.gmail.nevdmitry.accounttransfer.exception.AccountOperationException;
import com.gmail.nevdmitry.accounttransfer.exception.ErrorCode;

public class AccountOperationService {

    private final AccountOperationDao dao;

    public AccountOperationService(AccountOperationDao dao) {
        this.dao = dao;
    }

    /**
     * Transfers money from source account to the target one.
     *
     * @param sourceAccId source account id
     * @param targetAccId target account if
     * @param amount      amount in big decimal (min ETH doesn't fit to the double)
     *
     * @throws AccountOperationException if error occurs during money transfer
     */
    public void transfer(long sourceAccId, long targetAccId, double amount) throws AccountOperationException {
        validate(sourceAccId, targetAccId, amount);
        dao.transfer(sourceAccId, targetAccId, amount);
    }

    /**
     * Returns all trnsactions. May be parametrized by time frame in future
     *
     * @return collection of all transactions
     *
     * @throws AccountOperationException
     */
    public Collection<Transaction> transactions() {
        return dao.transactions();
    }

    public double accountBalance(long accountId) {
        return dao.accountBalance(accountId);
    }

    private static void validate(long sourceAccId, long targetAccId, double amount) throws AccountOperationException {
        if (sourceAccId == targetAccId) {
            throw new AccountOperationException(ErrorCode.WRONG_TARGET_ACCOUNT);
        } else if (Double.isNaN(amount) || Double.compare(amount, 0) <= 0) { //it is better to compare double not with 0
            throw new AccountOperationException(ErrorCode.NEGATIVE_AMOUNT);
        }
    }
}
