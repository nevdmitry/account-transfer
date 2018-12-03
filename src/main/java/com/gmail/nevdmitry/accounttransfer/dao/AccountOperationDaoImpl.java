package com.gmail.nevdmitry.accounttransfer.dao;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gmail.nevdmitry.accounttransfer.domain.Transaction;
import com.gmail.nevdmitry.accounttransfer.exception.AccountOperationException;
import com.gmail.nevdmitry.accounttransfer.exception.ErrorCode;

public class AccountOperationDaoImpl implements AccountOperationDao {

    private static final Logger logger = LoggerFactory.getLogger(AccountOperationDaoImpl.class);

    private static final AtomicLong transactionId = new AtomicLong(0);

    private final ConcurrentMap<Long /*AccId*/, Double /*Balance*/> accounts = new ConcurrentHashMap<>();

    private final ConcurrentLinkedQueue<Transaction> transactions = new ConcurrentLinkedQueue<>();

    //To emulate transactions use write lock
    private final ConcurrentHashMap<LockId, ReentrantReadWriteLock> locks = new ConcurrentHashMap<>();

    public AccountOperationDaoImpl() {
        //Here is initial data
        accounts.put(123L, 10000.11);
        accounts.put(321L, 20000d);
        accounts.put(111L, 30000d);
        accounts.put(222L, 40000d);
    }

    @Override
    public void transfer(long sourceAccId, long targetAccId, double amount) throws AccountOperationException {
        logger.debug("Account transfer from {} to {} amount={}", sourceAccId, targetAccId, amount);
        ReentrantReadWriteLock lock = locks.computeIfAbsent(new LockId(sourceAccId, targetAccId), l -> new ReentrantReadWriteLock());
        lock.writeLock().lock();
        try {
            double sourceBalance = accounts.get(sourceAccId);
            double targetBalance = accounts.get(targetAccId);

            if (Double.compare(sourceBalance, amount) < 0) {
                throw new AccountOperationException(ErrorCode.NO_ENOUGH_MONEY, "No enough money on account=" + sourceAccId);
            }
            sourceBalance -= amount;
            targetBalance += amount;

            accounts.put(sourceAccId, sourceBalance);
            accounts.put(targetAccId, targetBalance);
        } finally {
            lock.writeLock().unlock();
        }
        Transaction transaction = new Transaction(transactionId.incrementAndGet(), new Date(), sourceAccId, targetAccId, amount);
        transactions.add(transaction);
    }

    @Override
    public double accountBalance(long accountId) {
        return accounts.get(accountId);
    }

    @Override
    public Collection<Transaction> transactions() {
        return transactions;
    }


    private final class LockId {
        private final long account1;
        private final long account2;

        LockId(long account1, long account2) {
            this.account1 = account1;
            this.account2 = account2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            LockId lockId = (LockId) o;
            return account1 == lockId.account1 &&
                    account2 == lockId.account2 ||
                    account1 == lockId.account2 &&
                            account2 == lockId.account1;
        }

        @Override
        public int hashCode() {
            return Objects.hash(account1, account2);
        }
    }
}
