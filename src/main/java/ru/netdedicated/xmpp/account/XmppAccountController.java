package ru.netdedicated.xmpp.account;

import static ru.netdedicated.JsonUtil.json;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by artemz on 23.02.17.
 */
public class XmppAccountController {
    public XmppAccountController(final XmppAccountService xmppAccountService){
        get("/xmppAccount", ((request, response) -> {
            return xmppAccountService.list();
        }), json());
        post("/xmppAccount/new", ((request, response) ->
            xmppAccountService.create(
                    request.queryParams("username"),
                    request.queryParams("password"),
                    request.queryParams("host"),
                    Integer.parseInt(request.queryParams("port"))
            )
        ), json());
    }
}
