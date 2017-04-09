package ru.netdedicated.message;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import ru.netdedicated.AbstractService;
import ru.netdedicated.request.ChatRequest;

import java.util.List;

/**
 * Created by artemz on 04.12.16.
 */
public class MessageService extends AbstractService<Message>{
    @Override
    public Class<Message> getServiceClass() {
        return Message.class;
    }

    public MessageService(Datastore datastore) {
        super(datastore);
    }

    public List<Message> listByRequest(ChatRequest chatRequest){
        return getDatastore().createQuery(Message.class).field("request").equal(chatRequest).asList();
    }
    public Message create(String msg, ChatRequest chatRequest){
        Message message = new Message();
        message.setMessage(msg);
        message.setRequest(chatRequest);

        getDatastore().save(message);
        return message;
    }
    public Message createStatus(String msg, ChatRequest chatRequest){
        Message message = new Message();
        message.setMessage(msg);
        message.setRequest(chatRequest);
        message.setStatus(true);

        getDatastore().save(message);
        return message;
    }
}
