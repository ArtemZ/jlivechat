package ru.netdedicated.request;

import org.mongodb.morphia.Datastore;
import ru.netdedicated.AbstractService;
import ru.netdedicated.client.Client;
import ru.netdedicated.operator.Operator;

import java.util.List;

/**
 * Created by artemz on 04.12.16.
 */
public class RequestService extends AbstractService{

    public RequestService(Datastore datastore) {
        super(datastore);
    }

    public ChatRequest findByIdent(String ident){
        return getDatastore()
                .createQuery(ChatRequest.class)
                .field("ident")
                .equal(ident)
                .get();
    }
    public ChatRequest create(String ident, Client client){
        ChatRequest request = new ChatRequest();
        request.setIdent(ident);
        request.setClient(client);

        getDatastore().save(request);

        return request;
    }

    public List<ChatRequest> list(){
        return getDatastore().createQuery(ChatRequest.class).asList();
    }

    public void delete(ChatRequest request){
        getDatastore().delete(request);
    }
}
