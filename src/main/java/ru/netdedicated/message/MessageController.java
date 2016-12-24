package ru.netdedicated.message;

import ru.netdedicated.ResponseError;
import ru.netdedicated.request.ChatRequest;
import ru.netdedicated.request.RequestService;

import static ru.netdedicated.JsonUtil.json;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by artemz on 04.12.16.
 */
public class MessageController {
    public MessageController(final MessageService messageService, final RequestService requestService) {
        get("/messages/list/:ident", ((request, response) -> {
            ChatRequest chatRequest = requestService.findByIdent(request.params(":ident"));
            if (chatRequest == null){
                return new ResponseError("Chat with id %s not found", request.params(":ident"));
            } else {
                return messageService.listByRequest(chatRequest);
            }

        }

        ), json());
        post("/messages/create/:ident", ((request, response) -> {
            ChatRequest chatRequest = requestService.findByIdent(request.params(":ident"));
            return messageService.create(request.queryParams("message"), chatRequest);
        }), json());
    }
}
