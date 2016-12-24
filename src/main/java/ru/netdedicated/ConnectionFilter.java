package ru.netdedicated;

import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

/**
 * Created by artemz on 04.12.16.
 */
public class ConnectionFilter implements Filter {
    @Override
    public void handle(Request request, Response response) throws Exception {
        if (request.session().attribute("connID") == null){
            halt(401, "Your connection is not authorized (no session)");
        }
    }
}
