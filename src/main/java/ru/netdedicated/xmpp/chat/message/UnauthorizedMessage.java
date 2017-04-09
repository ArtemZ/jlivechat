package ru.netdedicated.xmpp.chat.message;

/**
 * Created by artemz on 02.04.17.
 */
public interface UnauthorizedMessage extends IncomingMessage{
    String getReason();
}
