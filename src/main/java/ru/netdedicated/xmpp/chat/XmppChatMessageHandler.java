package ru.netdedicated.xmpp.chat;

import lombok.SneakyThrows;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import ru.netdedicated.i18n.I18nService;
import ru.netdedicated.message.Message;
import ru.netdedicated.message.MessageService;
import ru.netdedicated.request.ChatRequest;
import ru.netdedicated.request.RequestService;
import ru.netdedicated.xmpp.chat.command.Command;
import ru.netdedicated.xmpp.chat.command.CommandType;
import ru.netdedicated.xmpp.chat.message.CommandMessage;
import ru.netdedicated.xmpp.chat.message.IncomingMessage;
import ru.netdedicated.xmpp.chat.message.TextMessage;
import ru.netdedicated.xmpp.chat.message.UnauthorizedMessage;
import ru.netdedicated.xmpp.pool.XmppConnectionPool;

/**
 * Created by artemz on 02.04.17.
 */
public class XmppChatMessageHandler {
    private final Chat chat;
    private final RequestService requestService;
    private final I18nService i18nService;
    private final MessageService messageService;
    private final String connId;

    public XmppChatMessageHandler(
            Chat chat,
            String connId,
            RequestService requestService,
            I18nService i18nService,
            MessageService messageService
    ) {
        this.chat = chat;
        this.requestService = requestService;
        this.i18nService = i18nService;
        this.connId = connId;
        this.messageService = messageService;
    }

    public void handle(IncomingMessage m){
        if (m instanceof UnauthorizedMessage){
            handle((UnauthorizedMessage)m);
        } else if (m instanceof CommandMessage){
            handle((CommandMessage)m);
        } else if (m instanceof TextMessage){
            handle((TextMessage)m);
        }

    }
    @SneakyThrows
    public void handle(UnauthorizedMessage m){
        chat.sendMessage(m.getReason());
    }
    @SneakyThrows
    public void handle(CommandMessage m){
        switch (m.getCommand().getType()){
            case CLOSE:
                requestService.close(requestService.findByIdent(connId));

                messageService.createStatus(i18nService.getStringForCode("client.support.request.closed"), requestService.findByIdent(connId));
                chat.sendMessage(i18nService.getStringForCode("operator.support.request.closed"));
                chat.close();
                XMPPTCPConnection conn = XmppConnectionPool.getInstance().borrowObject(connId);
                conn.disconnect();
                XmppConnectionPool.getInstance().invalidateObject(connId, conn);
                break;
            case HELLO:
                ChatRequest request = requestService.findByIdent(connId);
                requestService.assign(connId, m.getOperator());
                assert m.getOperator() != null;
                assert m.getOperator().getName() != null;
                assert i18nService != null;
                messageService.createStatus(
                        i18nService.getStringForCode("operator.joins.request", new String[] { m.getOperator().getName() } ),
                        request
                        );
                break;
            default:
                chat.sendMessage("Unknown command type: " + m.getCommand().getType().toString());
        }

    }
    public void handle(TextMessage m){
        messageService.create(m.getText(), requestService.findByIdent(connId));
    }
}
