package ru.netdedicated.duty;

import ru.netdedicated.exception.ValidationException;

import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;

/**
 * Created by artemz on 11.02.17.
 */
public class DutyController {
    public DutyController(final DutyService dutyService){
        get("/duty", ((request, response) -> dutyService.list() ));
        post("/duty/new", ((request, response) -> {
            Duty duty = null;
            try {
                duty = dutyService.create(Duty.fromRequest(request));
            } catch (ValidationException e){
                halt(500);
            }
            return duty;
        }));
    }
}
