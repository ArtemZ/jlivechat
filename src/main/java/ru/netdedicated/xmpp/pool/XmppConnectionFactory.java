package ru.netdedicated.xmpp.pool;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

/**
 * Created by artemz on 17.12.16.
 */
public class XmppConnectionFactory implements KeyedPooledObjectFactory<String, XMPPTCPConnection> {
    private XMPPTCPConnectionConfiguration configuration = null;


    @Override
    public PooledObject<XMPPTCPConnection> makeObject(String key) throws Exception {
        if (this.configuration == null){
            throw new NullPointerException("XmppConnectionFactory is not configured");
        }
        XMPPTCPConnection connection = new XMPPTCPConnection(configuration);
        connection.connect();
        /* resetting connection configuration so that no more connections would use it */
        //configuration = null;
        return new DefaultPooledObject<>(connection);
    }

    public void setUp(XMPPTCPConnectionConfiguration config){
        this.configuration = config;
        assert configuration != null;
    }

    @Override
    public void destroyObject(String s, PooledObject<XMPPTCPConnection> pooledObject) throws Exception {
        pooledObject.getObject().disconnect();
    }

    @Override
    public boolean validateObject(String s, PooledObject<XMPPTCPConnection> pooledObject) {
        return pooledObject.getObject().isConnected() && pooledObject.getObject().isAuthenticated();
    }

    @Override
    public void activateObject(String s, PooledObject<XMPPTCPConnection> pooledObject) throws Exception {
        XMPPTCPConnection conn = pooledObject.getObject();
        if (conn.isDisconnectedButSmResumptionPossible()){
            conn.connect();
        }
        if (!conn.isAuthenticated()){
            conn.login();
        }
    }

    public boolean hasConfiguration(){
        return this.configuration != null;
    }

    @Override
    public void passivateObject(String s, PooledObject<XMPPTCPConnection> pooledObject) throws Exception {

    }
}
