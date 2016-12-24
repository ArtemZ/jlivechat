package ru.netdedicated;


import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.ValidationExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.netdedicated.client.ClientService;
import ru.netdedicated.message.MessageController;
import ru.netdedicated.message.MessageService;
import ru.netdedicated.request.RequestController;
import ru.netdedicated.request.RequestService;
import spark.Spark;

/**
 * Hello world!
 *
 */
public class App 
{
    static Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });
        try {
            final Morphia morphia = new Morphia();
            morphia.mapPackage("ru.netdedicated.client", true);
            morphia.mapPackage("ru.netdedicated.message", true);
            morphia.mapPackage("ru.netdedicated.operator", true);
            morphia.mapPackage("ru.netdedicated.request", true);
            new ValidationExtension(morphia);

            final Datastore datastore = morphia.createDatastore(new MongoClient("localhost"), "jlivechat");

            RequestService requestService = new RequestService(datastore);
            new MessageController(new MessageService(datastore), requestService);

            new RequestController(requestService, new ClientService(datastore));

        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }

        //get("/", (request, response) -> "Hello World");
        //get("");

    }
}
