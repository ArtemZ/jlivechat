package ru.netdedicated.xmpp.account;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Xmpp Account to be used for sending messages to Operators
 */
@Entity
public class XmppAccount {
    @Id
    @Getter
    private String id = new ObjectId().toHexString();
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String host;
    @Getter
    @Setter
    private Integer port = 5222;
    @Getter
    @Setter
    private String takenBy = null; // Session id
}
