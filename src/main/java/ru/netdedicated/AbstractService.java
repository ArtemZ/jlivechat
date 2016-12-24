package ru.netdedicated;

import com.mongodb.DBCollection;
import org.mongodb.morphia.Datastore;

/**
 * Created by artemz on 05.12.16.
 */
public abstract class AbstractService {
    private final Datastore datastore;

    public AbstractService(Datastore datastore) {
        this.datastore = datastore;
    }

    public Datastore getDatastore(){
        return datastore;
    }

}
