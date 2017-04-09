package ru.netdedicated.xmpp.pool;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

/**
 * Created by artemz on 17.12.16.
 */
public class XmppConnectionPool extends GenericKeyedObjectPool<String, XMPPTCPConnection> {
    private XmppConnectionPool(KeyedPooledObjectFactory<String, XMPPTCPConnection> factory) {
        super(factory);
    }

    private XmppConnectionPool(KeyedPooledObjectFactory<String, XMPPTCPConnection> factory, GenericKeyedObjectPoolConfig config) {
        super(factory, config);
    }

    private static class InnerXmppConnectionPool {
        private static final XmppConnectionPool INSTANCE = new XmppConnectionPool(new XmppConnectionFactory(), getConfig());
        private static GenericKeyedObjectPoolConfig getConfig() {
            GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
            config.setMaxTotalPerKey(1);
            return config;
        }
    }

    public static XmppConnectionPool getInstance(){
        return InnerXmppConnectionPool.INSTANCE;
    }

/*    @Override
        public void addObject(K key) throws Exception {
                assertOpen();
                register(key);
                try {
                        PooledObject<T> p = create(key);
                        addIdleObject(key, p);
                    } finally {
                        deregister(key);
                    }
            }*/

    public XMPPTCPConnection addConnection(String connId, XMPPTCPConnectionConfiguration config) throws Exception{
        assert config != null;
        ((XmppConnectionFactory)getFactory()).setUp(config);
        preparePool(connId);

        addObject(connId);

        return borrowObject(connId);
    }


}
