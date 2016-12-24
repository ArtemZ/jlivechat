package ru.netdedicated.request;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import ru.netdedicated.client.Client;
import ru.netdedicated.operator.Operator;

import java.util.Date;

/**
 * Created by artemz on 04.12.16.
 */
@Entity("requests")
public class ChatRequest {
    @Id
    @Getter
    private ObjectId id;
    @Getter
    @Setter
    private String ident;
    @Getter
    @Setter
    private Operator op;
    @Getter
    @Setter
    @Reference
    private Client client;
    @Getter
    @Setter
    private Date dateCreated = new Date();
    @Getter
    @Setter
    private Boolean smsSent = false;
    @Getter
    @Setter
    private Boolean closed = false;
}
