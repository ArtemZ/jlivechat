package ru.netdedicated.xmpp.chat.command;

/**
 * Created by artemz on 02.04.17.
 */
public class CloseCommand implements Command {
    @Override
    public CommandType getType() {
        return CommandType.CLOSE;
    }

    @Override
    public String[] getArgs() {
        return new String[0];
    }
}
