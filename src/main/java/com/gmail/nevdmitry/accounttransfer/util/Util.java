package com.gmail.nevdmitry.accounttransfer.util;

import spark.Filter;
import spark.Request;
import spark.Response;

public class Util {

    public static Filter addTrailingSlashes = (Request request, Response response) -> {
        if (!request.pathInfo().endsWith("/")) {
            response.redirect(request.pathInfo() + "/");
        }
    };
}
