package ru.netdedicated.xmpp.chat.message;

import ru.netdedicated.xmpp.chat.command.Command;

/**
 * Created by artemz on 02.04.17.
 */
public interface CommandMessage extends AuthorizedMessage {
    Command getCommand();
}
