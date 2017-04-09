package ru.netdedicated.request;

import org.mongodb.morphia.Datastore;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import ru.netdedicated.AbstractService;
import ru.netdedicated.client.Client;
import ru.netdedicated.jobs.XmppChatJob;
import ru.netdedicated.operator.Operator;

import java.util.List;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by artemz on 04.12.16.
 */
public class RequestService extends AbstractService<ChatRequest>{

    private final Scheduler scheduler;

    public RequestService(Datastore datastore, Scheduler sched) {

        super(datastore);
        this.scheduler = sched;
    }

    @Override
    public Class<ChatRequest> getServiceClass() {
        return ChatRequest.class;
    }

    public ChatRequest findByIdent(String ident){
        return getDatastore()
                .createQuery(ChatRequest.class)
                .field("ident")
                .equal(ident)
                .get();
    }
    public ChatRequest create(String ident, Client client) throws SchedulerException{
        ChatRequest request = new ChatRequest();
        request.setIdent(ident);
        request.setClient(client);

        getDatastore().save(request);

        scheduler.scheduleJob(newJob(XmppChatJob.class).build(), newTrigger()
                        .withIdentity("Chat Request Trigger")
                        .withSchedule(simpleSchedule().withRepeatCount(0))
                        .startNow()
                        .usingJobData("connId", ident)
                        .build()
        );


        return request;
    }

    public void assign(String ident, Operator op){
        ChatRequest request = findByIdent(ident);
        request.setOp(op);
        getDatastore().save(request);
    }

    public void delete(ChatRequest request){
        getDatastore().delete(request);
    }

    public void close(ChatRequest request){
        request.setClosed(true);
        getDatastore().save(request);
    }
}
