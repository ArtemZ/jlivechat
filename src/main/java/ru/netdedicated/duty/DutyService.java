package ru.netdedicated.duty;

import org.mongodb.morphia.Datastore;
import ru.netdedicated.AbstractService;
import ru.netdedicated.operator.Operator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by artemz on 24.12.16.
 */
public class DutyService extends AbstractService<Duty> {

    @Override
    public Class<Duty> getServiceClass() {
        return Duty.class;
    }

    public DutyService(Datastore datastore) {
        super(datastore);
    }
    public int getCurrentHour(Date now){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        System.out.println("Current hour: " + calendar.get(Calendar.HOUR_OF_DAY));
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    private List<Duty> getCloseDuties(Date now){
        return getDatastore().createQuery(Duty.class)
                .disableValidation()
                .filter("startDate <=", now)
                .filter("endDate >=", now)
                //.filter("startHour <=", getCurrentHour(now))
                .asList();
    }

    public Duty findCurrentDuty(Date now){
        List<Duty> currentDuties = getCloseDuties(now).stream()
                .filter( duty -> new TodayDutyPeriod(duty.getStartHour(), duty.getEndHour()).isCurrentDuty() )
                .collect(Collectors.toList());
        if (currentDuties.size() > 0){
            return currentDuties.get(0);
        }
        return null;

    }

    public void addOperator(Duty duty, Operator operator){
        duty.addOperator(operator);
        getDatastore().save(duty);
    }

    private class TodayDutyPeriod {
        private List<Integer> hours = new ArrayList<>();

        public TodayDutyPeriod(Integer startHour, Integer endHour) {
            hours = new ArrayList<>();
            hours.add(startHour);
            while ((hours.get(hours.size() - 1) + 1) != endHour){
                int prevHour = hours.get(hours.size() - 1);
                if (prevHour == 23){
                    hours.add(0);
                } else {
                    hours.add(prevHour + 1);
                }
            }
        }

        public boolean isCurrentDuty(){
            return hours.contains(getCurrentHour(new Date()));
        }
    }
}
