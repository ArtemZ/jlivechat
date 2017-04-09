package ru.netdedicated.xmpp.pool;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.net.ConnectException;

/**
 * Created by artemz on 18.12.16.
 */
public class XmppChatFactory implements KeyedPooledObjectFactory<String, Chat> {
    private String chatWith;
    private ChatMessageListener listener;

    @Override
    public PooledObject<Chat> makeObject(String connId) throws Exception {
        if (chatWith == null) throw new NullPointerException("Chat with is not specified");
        XMPPTCPConnection connection = XmppConnectionPool.getInstance().borrowObject(connId);
        if (connection == null){
            throw new ConnectException("No connection with id: " + connId);
        }
        ChatManager manager = ChatManager.getInstanceFor(connection);
        Chat chat = manager.createChat(chatWith, listener);
        //chatWith = null;
        //listener = null;
        XmppConnectionPool.getInstance().returnObject(connId, connection);
        return new DefaultPooledObject<>(chat);
    }

    public void setUpChatWith(String jid, ChatMessageListener messageListener){
        this.listener = messageListener;
        chatWith = jid;
    }

    @Override
    public void destroyObject(String s, PooledObject<Chat> pooledObject) throws Exception {
        pooledObject.getObject().close();
    }

    @Override
    public boolean validateObject(String s, PooledObject<Chat> pooledObject) {
        return true;
    }

    @Override
    public void activateObject(String s, PooledObject<Chat> pooledObject) throws Exception {

    }

    @Override
    public void passivateObject(String s, PooledObject<Chat> pooledObject) throws Exception {

    }
}
