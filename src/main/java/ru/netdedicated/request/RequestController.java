package ru.netdedicated.request;

import ru.netdedicated.client.Client;
import ru.netdedicated.client.ClientService;

import java.util.*;
import java.util.stream.Collectors;

import static ru.netdedicated.JsonUtil.json;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by artemz on 04.12.16.
 */
public class RequestController {
    public RequestController(final RequestService requestService, final ClientService clientService) {
        get("/requests", (request, response) -> {
            return requestService.list();
        }, json());
        post("/requests/new", (request, response) -> {

            String uuid = UUID.randomUUID().toString().replaceAll("[-+.^:,]", "");
            List<Locale> locales = null;
            if (request.headers("Accept-Language") == null || request.headers("Accept-Language").isEmpty()){
                locales = new ArrayList<>();
                locales.add(new Locale("en"));
            } else {
                locales = Locale.LanguageRange.parse(request.headers("Accept-Language")).stream().map(rang‌​e ->
                        new Locale(rang‌​e.getRange())).collect(Collectors.toList());
            }
            Client client = clientService.create(
                    request.queryParams("name"),
                    request.queryParams("email"),
                    request.ip(),
                    locales.get(0)
                    );
            return requestService.create(uuid, client );
        }, json());
    }
}
