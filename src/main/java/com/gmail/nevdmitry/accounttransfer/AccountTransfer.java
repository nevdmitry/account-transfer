package com.gmail.nevdmitry.accounttransfer;

import com.gmail.nevdmitry.accounttransfer.api.Path;
import com.gmail.nevdmitry.accounttransfer.controller.AccountTransferController;
import com.gmail.nevdmitry.accounttransfer.exception.AccountOperationException;
import com.gmail.nevdmitry.accounttransfer.util.Util;

import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.port;

public class AccountTransfer {
    public static void main(String[] args) {
        // Configure Spark
        port(8877);

        // Set up before-filters (called before each get/post)
        before("*", Util.addTrailingSlashes);

        post(Path.TRANSFER, AccountTransferController.transfer);

        get(Path.TRANSACTIONS, AccountTransferController.transactions);
        get(Path.BALACE, AccountTransferController.balance);

        exception(AccountOperationException.class, (e, request, response) -> {
            response.status(404);
            response.body("error:" + e.getCode());
        });

    }
}
