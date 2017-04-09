package ru.netdedicated.duty;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.RETURNS_SELF;
import static org.mockito.Mockito.when;

/**
 * Created by artemz on 31.12.16.
 */
@RunWith(MockitoJUnitRunner.class)
public class DutyTest {
    DutyService dutyService;

    @Mock Datastore datastore;

    @Before
    public void initDutyService(){
        dutyService = new DutyService(datastore);
    }

    private Date addHoursToDate(Date now, int hours){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    private int getHour(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    @Test
    public void testGettingCurrentHour(){
        assert dutyService.getCurrentHour(new Date()) == Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }
    @Test
    @SuppressWarnings("unchecked")
    public void testFindingCurrentDutyStartsEndsToday(){
        Date date = new Date();
        System.out.println(addHoursToDate(date, 5));
        Duty duty = new Duty();
        duty.setEndDate(addHoursToDate(date, 5));
        duty.setStartDate(addHoursToDate(date, -10));
        duty.setStartHour(getHour(addHoursToDate(date, -1)));
        duty.setEndHour(getHour(addHoursToDate(date, 1)));

        List<Duty> duties = Arrays.asList(duty);

        Query<Duty> query = Mockito.mock(Query.class, RETURNS_SELF);
        when(query.asList()).thenReturn(duties);

        when(datastore.createQuery(Duty.class)).thenReturn(query);

        assert dutyService.findCurrentDuty(date) == duty;
    }
    @SuppressWarnings("unchecked")
    @Test
    public void testDutyEndsSoonerToday(){
        Date date = new Date();

        Duty duty = new Duty();
        duty.setEndDate(addHoursToDate(date, 25));
        duty.setStartDate(addHoursToDate(date, -10));
        duty.setStartHour(getHour(addHoursToDate(date, -5)));
        duty.setEndHour(getHour(addHoursToDate(date, -1)));

        List<Duty> duties = Arrays.asList(duty);

        Query<Duty> query = Mockito.mock(Query.class, RETURNS_SELF);
        when(query.asList()).thenReturn(duties);

        when(datastore.createQuery(Duty.class)).thenReturn(query);

  //      Duty foundDuty = dutyService.findCurrentDuty(date);
//        System.out.println(foundDuty.getEndHour());

        Assert.assertNull(dutyService.findCurrentDuty(date));
    }
 /*   @Test
    @SuppressWarnings("unchecked")
    public void testDutyStartsLaterToday(){
        Date date = new Date();

        Duty duty = new Duty();
        duty.setEndDate(addHoursToDate(date, 25));
        duty.setStartDate(addHoursToDate(date, -10));
        duty.setStartHour(getHour(addHoursToDate(date, 1)));
        duty.setEndHour(getHour(addHoursToDate(date, 15)));

        List<Duty> duties = Arrays.asList(duty);

        Query<Duty> query = Mockito.mock(Query.class, RETURNS_SELF);
        when(query.asList()).thenReturn(duties);

        when(datastore.createQuery(Duty.class)).thenReturn(query);

        Assert.assertNull(dutyService.findCurrentDuty(date));

    }*/
}
