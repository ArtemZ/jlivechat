package ru.netdedicated;

import com.mongodb.operation.UpdateOperation;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import ru.netdedicated.exception.ValidationException;
import spark.Request;

import java.util.List;

/**
 * Created by artemz on 05.12.16.
 */
public abstract class AbstractService<T> {
    private final Datastore datastore;

    public abstract Class<T> getServiceClass();

    public AbstractService(Datastore datastore) {
        this.datastore = datastore;
    }

    public Datastore getDatastore(){
        return datastore;
    }

    public T get(String id){
        return getDatastore().find(getServiceClass()).field("id").equal(id).get();
    }

    public List<T> list(){
        return getDatastore().createQuery(getServiceClass()).asList();
    }
    public T create(T instance) throws ValidationException{
        getDatastore().save(instance);
        return instance;
    }
    public Query<T> getQuery(){
        return getDatastore().createQuery(getServiceClass());
    }
    public UpdateOperations<T> getUpdateOperations(){
        return getDatastore().createUpdateOperations(getServiceClass());
    }
}
