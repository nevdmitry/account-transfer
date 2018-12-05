package com.gmail.nevdmitry.accounttransfer.controller;

import spark.Request;
import spark.Response;
import spark.Route;

import com.gmail.nevdmitry.accounttransfer.dao.AccountOperationDaoImpl;
import com.gmail.nevdmitry.accounttransfer.dto.TransferDTO;
import com.gmail.nevdmitry.accounttransfer.service.AccountOperationService;
import com.google.gson.Gson;

public class AccountTransferController {

    //No IoC just to keep it simple
    private static final AccountOperationService service = new AccountOperationService(new AccountOperationDaoImpl());

    public static Route transfer = (Request request, Response response) -> {
        TransferDTO transfer = new Gson().fromJson(request.body(), TransferDTO.class);
        service.transfer(transfer.getSourceAccId(), transfer.getTargetAccId(), transfer.getAmount());
        return "status:ok";
    };

    public static Route transactions = (Request request, Response response) -> {
        response.type("application/json");
        return new Gson().toJsonTree(service.transactions());
    };

    public static Route balance = (Request request, Response response) -> {
        response.type("application/json");
        return new Gson().toJson(service.accountBalance(Long.valueOf(request.queryParams("accountId"))), double.class);
    };

}
