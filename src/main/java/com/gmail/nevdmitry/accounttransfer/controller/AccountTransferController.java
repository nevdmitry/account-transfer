package com.gmail.nevdmitry.accounttransfer.controller;

import spark.Request;
import spark.Response;
import spark.Route;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import com.gmail.nevdmitry.accounttransfer.dao.AccountOperationDaoImpl;
import com.gmail.nevdmitry.accounttransfer.dto.TransferDTO;
import com.gmail.nevdmitry.accounttransfer.service.AccountOperationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class AccountTransferController {

//    private final static Gson gson = new GsonBuilder().
//            registerTypeAdapter(Double.class, (JsonSerializer<Double>) (src, typeOfSrc, context) -> {
//                //Assume balance precision 2. Need to rewrite to store BigDecimal instead of Double
//                return new JsonPrimitive(new BigDecimal(src).setScale(2, RoundingMode.HALF_UP).doubleValue());
//            }).create();

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
