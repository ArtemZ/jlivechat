package ru.netdedicated.client;

import org.mongodb.morphia.Datastore;
import ru.netdedicated.AbstractService;

import java.util.Locale;

/**
 * Created by artemz on 10.12.16.
 */
public class ClientService extends AbstractService {
    public ClientService(Datastore datastore) {
        super(datastore);
    }
    public Client create(String name, String email, String ip, Locale locale){
        Client client = new Client();
        client.setName(name);
        client.setEmail(email);
        client.setIp(ip);
        client.setLocale(locale);
        getDatastore().save(client);
        return client;
    }

}
