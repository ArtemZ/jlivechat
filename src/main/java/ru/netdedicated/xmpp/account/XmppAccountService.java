package ru.netdedicated.xmpp.account;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import ru.netdedicated.AbstractService;

import java.util.List;

/**
 * Created by artemz on 03.01.17.
 */
public class XmppAccountService extends AbstractService<XmppAccount> {
    @Override
    public Class<XmppAccount> getServiceClass() {
        return XmppAccount.class;
    }

    public XmppAccountService(Datastore datastore) {
        super(datastore);
    }
    public XmppAccount selectAvailable(){
        Query<XmppAccount> query = getDatastore().createQuery(XmppAccount.class);
        List<XmppAccount> freeAccounts = query.field("takenBy").equal(null).asList();
        if (freeAccounts.size() > 0){
            return freeAccounts.get(0);
        }
        return null;
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
