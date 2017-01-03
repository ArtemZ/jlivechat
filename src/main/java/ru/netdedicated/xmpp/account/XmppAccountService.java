package ru.netdedicated.xmpp.account;

import org.mongodb.morphia.Datastore;
import ru.netdedicated.AbstractService;

/**
 * Created by artemz on 03.01.17.
 */
public class XmppAccountService extends AbstractService {
    public XmppAccountService(Datastore datastore) {
        super(datastore);
    }
    public XmppAccount create(String username, String password, String host, Integer port){
        XmppAccount account = new XmppAccount();
        account.setUsername(username);
        account.setPassword(password);
        account.setHost(host);
        account.setPort(port);

        getDatastore().save(account);
        return account;
    }
}
