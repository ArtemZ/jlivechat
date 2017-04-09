package ru.netdedicated.xmpp.chat.message;

import ru.netdedicated.operator.Operator;

/**
 * Created by artemz on 02.04.17.
 */
public class TextMessage implements AuthorizedMessage {
    private final Operator op;
    private final String jid;
    private final String text;

    public TextMessage(Operator op, String jid, String text) {
        this.op = op;
        this.jid = jid;
        this.text = text;
    }

    @Override
    public Operator getOperator() {
        return op;
    }

    @Override
    public String getSenderJid() {
        return jid;
    }

    public String getText(){
        return text;
    }
}
