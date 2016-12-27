package ru.netdedicated.operator;

import org.mongodb.morphia.Datastore;
import ru.netdedicated.AbstractService;

import java.util.List;

/**
 * Created by artemz on 24.12.16.
 */
public class OperatorService extends AbstractService {
    public OperatorService(Datastore datastore) {
        super(datastore);
    }
    public List<Operator> listAvailable(){

    }
}
