package ru.netdedicated.xmpp.chat;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import ru.netdedicated.i18n.I18nService;
import ru.netdedicated.message.MessageService;
import ru.netdedicated.operator.Operator;
import ru.netdedicated.operator.OperatorService;
import ru.netdedicated.request.ChatRequest;
import ru.netdedicated.request.RequestService;
import ru.netdedicated.xmpp.chat.message.XmppMessageFactory;

/**
 * Created by artemz on 03.01.17.
 */
public class XmppChatMessageListener implements ChatMessageListener {
    private final MessageService messageService;
    private final RequestService requestService;
    private final OperatorService operatorService;
    private final I18nService i18nService;
    private final String connId;

    public XmppChatMessageListener(
            String connId,
            MessageService messageService,
            RequestService requestService,
            OperatorService operatorService,
            I18nService i18nService
    ) {
        this.messageService = messageService;
        this.requestService = requestService;
        this.operatorService = operatorService;
        this.i18nService = i18nService;
        this.connId = connId;
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        XmppMessageFactory f = new XmppMessageFactory(message, connId, requestService, operatorService);
        new XmppChatMessageHandler(
                chat,
                connId,
                requestService,
                i18nService,
                messageService
        ).handle(f.getMessage());

    }
}
