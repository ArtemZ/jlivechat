package ru.netdedicated.duty;

import org.mongodb.morphia.Datastore;
import ru.netdedicated.AbstractService;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by artemz on 24.12.16.
 */
public class DutyService extends AbstractService {
    public DutyService(Datastore datastore) {
        super(datastore);
    }
    public Duty findCurrentDuty(Date now){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        getDatastore().createQuery(Duty.class)
                .disableValidation()
                .filter("startDate <=", now)
                .filter("endDate >=", now)
                .filter("startHour <=", calendar.get(Calendar.HOUR_OF_DAY))

    }
}
