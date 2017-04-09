package ru.netdedicated.xmpp.chat.message;

import ru.netdedicated.operator.Operator;

/**
 * Created by artemz on 02.04.17.
 */
public interface AuthorizedMessage extends IncomingMessage {
    Operator getOperator();
}
