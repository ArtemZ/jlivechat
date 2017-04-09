package ru.netdedicated.operator;

import org.mongodb.morphia.Datastore;
import ru.netdedicated.AbstractService;
import ru.netdedicated.duty.Duty;

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

    public Operator getByJid(String jid){
        return getDatastore().createQuery(Operator.class).field("jabber").equal(jid).get();
    }

    public void addDuty(Duty duty, Operator operator){
        operator.addDuty(duty);
        getDatastore().save(operator);
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
