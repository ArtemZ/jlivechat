package ru.netdedicated.duty;

import ru.netdedicated.exception.ValidationException;
import ru.netdedicated.operator.Operator;
import ru.netdedicated.operator.OperatorService;

import static ru.netdedicated.JsonUtil.json;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;

/**
 * Created by artemz on 11.02.17.
 */
public class DutyController {
    public DutyController(final DutyService dutyService, final OperatorService operatorService){
        get("/duty", ((request, response) -> dutyService.list() ), json());
        post("/duty/new", ((request, response) -> {
            Duty duty = null;
            try {
                duty = dutyService.create(Duty.fromRequest(request));
            } catch (ValidationException e) {
                halt(500);
            }
            return duty;
        }), json());
        post("/duty/:id/addOperator/:op_id", ((request, response) -> {
            Duty duty = dutyService.get(request.params(":id"));
            Operator op = operatorService.get(request.params(":op_id"));
            if (duty == null){
                halt(404, "Duty with id " + request.params(":id") + " was not found");
            } else if (op == null) {
                halt(404, "Operator with id " + request.params(":op_id") + " was not found");
            } else {
                dutyService.addOperator(duty, op);
            }
            return duty;
        }), json());
    }
}
