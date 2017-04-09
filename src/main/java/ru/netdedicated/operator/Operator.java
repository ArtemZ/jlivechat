package ru.netdedicated.operator;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import ru.netdedicated.duty.Duty;
import ru.netdedicated.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by artemz on 04.12.16.
 */
@Entity("operators")
public class Operator {
    @Id
    @Getter
    private String id = new ObjectId().toHexString();
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    @Indexed(unique = true)
    private String jabber;
    @Getter
    @Setter
    @Indexed(unique = true)
    private String phoneNumber;
    @Getter
    @Reference
    private List<Message> messages;
    @Getter
    @Reference(lazy = true)
    private List<Duty> duties = new ArrayList<>();

    public void addDuty(Duty duty){
        duties.add(duty);
    }
}
