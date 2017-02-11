package ru.netdedicated.operator;

import org.mongodb.morphia.Datastore;
import ru.netdedicated.AbstractService;

import java.util.List;

/**
 * Created by artemz on 24.12.16.
 */
public class OperatorService extends AbstractService<Operator> {

    @Override
    public Class<Operator> getServiceClass() {
        return Operator.class;
    }

    public OperatorService(Datastore datastore) {
        super(datastore);
    }


    public List<Operator> listAvailable(){
        return null;
    }

    public Operator create(String name, String jabber, String phoneNumber){
        Operator operator = new Operator();
        operator.setJabber(jabber);
        operator.setName(name);
        operator.setPhoneNumber(phoneNumber);

        getDatastore().save(operator);

        return operator;
    }
}
