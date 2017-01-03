package ru.netdedicated.duty;

import org.mongodb.morphia.Datastore;
import ru.netdedicated.AbstractService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by artemz on 24.12.16.
 */
public class DutyService extends AbstractService {

    public DutyService(Datastore datastore) {
        super(datastore);
    }
    public int getCurrentHour(Date now){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    private List<Duty> getCloseDuties(Date now){
        return getDatastore().createQuery(Duty.class)
                .disableValidation()
                .filter("startDate <=", now)
                .filter("endDate >=", now)
                .filter("startHour <=", getCurrentHour(now))
                .asList();
    }
    public Duty findCurrentDuty(Date now){
        List<Duty> currentDuties = getCloseDuties(now).stream()
                .filter( duty -> duty.getEndHour() > getCurrentHour(now) || duty.getStartHour() < duty.getEndHour())
                .collect(Collectors.toList());
        if (currentDuties.size() > 0){
            return currentDuties.get(0);
        }
        return null;

    }
}
