package ru.netdedicated.duty;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.netdedicated.exception.ValidationException;
import ru.netdedicated.operator.Operator;
import spark.Request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by artemz on 24.12.16.
 */
@Entity
public class Duty {

    static Logger logger = LoggerFactory.getLogger(Duty.class);

    @Id
    @Getter
    private ObjectId id;
    @Getter
    @Setter
    @NotEmpty
    private Date startDate;
    @Getter
    @Setter
    @NotEmpty
    private Date endDate;
    @Getter
    @Setter
    @NotEmpty
    /* Day hours this duty is active */
    private Integer startHour;
    @Getter
    @Setter
    @NotEmpty
    /* If end hour < start hour then it is belonging to the next day */
    private Integer endHour;
    @Getter
    @Reference
    private List<Operator> operators;

    public void addOperator(Operator op){
        operators.add(op);
    }

    public static Duty fromRequest(Request request) throws ValidationException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Duty duty = new Duty();
        try {
            duty.setStartDate(sdf.parse(request.queryParams("startDate")));
        } catch (ParseException e){
            logger.error("Unable to parse start date: " + request.queryParams("startDate"), e);
            throw new ValidationException(e.getMessage());
        }
        try {
            duty.setEndDate(sdf.parse(request.queryParams("endDate")));
        } catch (ParseException e){
            logger.error("Unable to parse end date: " + request.queryParams("endDate"), e);
            throw new ValidationException(e.getMessage());
        }
        duty.setStartHour(Integer.parseInt(request.queryParams("startHour")));
        duty.setEndHour(Integer.parseInt(request.queryParams("endHour")));
        return duty;
    }
}
