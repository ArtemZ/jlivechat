package ru.netdedicated.operator;

import static ru.netdedicated.JsonUtil.json;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by artemz on 11.02.17.
 */
public class OperatorController {
    public OperatorController(final OperatorService operatorService){
        get("/operators", ((request, response) ->  operatorService.list() ), json());
        post("/operators/new", ((request, response) -> {
            return operatorService.create(
                    request.queryParams("name"),
                    request.queryParams("jabber"),
                    request.queryParams("phoneNumber")
            );
        } ), json());
    }
}
