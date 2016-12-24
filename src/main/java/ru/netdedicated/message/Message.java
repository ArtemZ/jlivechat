package ru.netdedicated.message;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import ru.netdedicated.operator.Operator;
import ru.netdedicated.request.ChatRequest;

import java.util.Date;

/**
 * Created by artemz on 04.12.16.
 */
@Entity("messages")
public class Message {
    @Id
    @Getter
    private ObjectId id;
    @Getter
    @Setter
    @NotBlank
    private String message;
    @Getter
    @Setter
    private Date dateCreated = new Date();
    @Getter
    @Setter
    private Boolean status = false;
    @Reference
    private Operator op;
    @Getter
    @Setter
    @Reference
    @NotEmpty
    private ChatRequest request;
}
