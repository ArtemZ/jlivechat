package ru.netdedicated.duty;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import ru.netdedicated.operator.Operator;

import java.util.Date;
import java.util.List;

/**
 * Created by artemz on 24.12.16.
 */
@Entity
public class Duty {
    @Id
    @Getter
    private ObjectId id;
    @Getter
    @Setter
    private Date startDate;
    @Getter
    @Setter
    private Date endDate;
    @Getter
    @Setter
    /* Day hours this duty is active */
    private Integer startHour;
    @Getter
    @Setter
    /* If end hour < start hour then it is belonging to the next day */
    private Integer endHour;
    @Getter
    @Reference
    private List<Operator> operators;
}
