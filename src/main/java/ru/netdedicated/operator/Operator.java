package ru.netdedicated.operator;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import ru.netdedicated.message.Message;

import java.util.List;

/**
 * Created by artemz on 04.12.16.
 */
@Entity("operators")
public class Operator {
    @Id
    private ObjectId id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String jabber;
    @Getter
    @Setter
    private String phoneNumber;
    @Getter
    @Reference
    private List<Message> messages;
}
