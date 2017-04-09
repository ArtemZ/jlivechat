package ru.netdedicated.xmpp.chat.command;

/**
 * Created by artemz on 02.04.17.
 */
public interface Command {
    CommandType getType();
    String[] getArgs();
}
