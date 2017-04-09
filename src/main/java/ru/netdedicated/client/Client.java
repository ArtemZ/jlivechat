package ru.netdedicated.client;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import ru.netdedicated.request.ChatRequest;

import java.util.List;
import java.util.Locale;

/**
 * Created by artemz on 05.12.16.
 */
@Entity("clients")
public class Client {
    @Id
    @Getter
    private String id = new ObjectId().toHexString();
    //@NotBlank
    @Getter
    @Setter
    private String name;
    //@Email
    @Getter
    @Setter
    private String email;
    //@NotBlank
    @Getter
    @Setter
    private String ip;
    @Getter
    @Setter
    private Locale locale;
    @Getter
    @Reference
    private List<ChatRequest> requests;
}
