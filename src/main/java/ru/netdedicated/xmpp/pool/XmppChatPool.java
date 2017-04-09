package ru.netdedicated.xmpp.pool;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;

/**
 * Created by artemz on 19.12.16.
 */
public class XmppChatPool extends GenericKeyedObjectPool<String, Chat> {
    private XmppChatPool(KeyedPooledObjectFactory<String, Chat> factory) {
        super(factory);
    }

    private XmppChatPool(KeyedPooledObjectFactory<String, Chat> factory, GenericKeyedObjectPoolConfig config) {
        super(factory, config);
    }

    private static class InnerXmppChatPool {
        private static final XmppChatPool INSTANCE = new XmppChatPool(new XmppChatFactory());
        private static GenericKeyedObjectPoolConfig getConfig(){
            GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
            config.setMaxTotalPerKey(1);
            return config;
        }
    }
    public static XmppChatPool getInstance() {
        return InnerXmppChatPool.INSTANCE;
    }

    public Chat addChat(String connId, String with, ChatMessageListener listener) throws Exception{
        ((XmppChatFactory) getFactory()).setUpChatWith(with, listener);
        preparePool(connId);
        addObject(connId);
        return borrowObject(connId);

    }

}
