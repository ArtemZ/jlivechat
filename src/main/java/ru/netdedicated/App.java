package ru.netdedicated;


import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.ValidationExtension;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.netdedicated.client.ClientService;
import ru.netdedicated.duty.DutyController;
import ru.netdedicated.duty.DutyService;
import ru.netdedicated.message.MessageController;
import ru.netdedicated.message.MessageService;
import ru.netdedicated.operator.OperatorController;
import ru.netdedicated.operator.OperatorService;
import ru.netdedicated.request.RequestController;
import ru.netdedicated.request.RequestService;
import ru.netdedicated.xmpp.account.XmppAccountController;
import ru.netdedicated.xmpp.account.XmppAccountService;
import spark.Spark;

import static spark.Spark.before;

/**
 * Hello world!
 */
public class App {
    static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws SchedulerException {
        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });
        final Morphia morphia = new Morphia();
        morphia.mapPackage("ru.netdedicated.client", true);
        morphia.mapPackage("ru.netdedicated.message", true);
        morphia.mapPackage("ru.netdedicated.operator", true);
        morphia.mapPackage("ru.netdedicated.request", true);
        morphia.mapPackage("ru.netdedicated.xmpp.account", true);
        new ValidationExtension(morphia);

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();

        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost"), "jlivechat");
        DutyService dutyService = new DutyService(datastore);
        XmppAccountService accountService = new XmppAccountService(datastore);
        MessageService messageService = new MessageService(datastore);

        scheduler.getContext().put("dutyService", dutyService);
        scheduler.getContext().put("xmppAccountService", accountService);
        scheduler.getContext().put("messageService", messageService);
        RequestService requestService = new RequestService(datastore, scheduler, accountService);
        OperatorService operatorService = new OperatorService(datastore);
        scheduler.getContext().put("requestService", requestService);
        scheduler.getContext().put("operatorService", operatorService);


        before((request, response) -> response.type("application/json"));

        new MessageController(messageService, requestService);

        new RequestController(requestService, new ClientService(datastore));

        new OperatorController(operatorService);

        new DutyController(dutyService, operatorService);

        new XmppAccountController(accountService);

        datastore.ensureIndexes();

    }
}
