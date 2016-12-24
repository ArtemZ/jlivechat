package ru.netdedicated.xmpp.account;

import org.mongodb.morphia.annotations.Entity;

/**
 * Created by artemz on 18.12.16.
 */
@Entity
public class XmppAccount {
    private String username;
    private String password;
    private String host;
}
