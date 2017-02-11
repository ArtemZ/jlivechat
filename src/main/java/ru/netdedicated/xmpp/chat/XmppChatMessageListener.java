package ru.netdedicated.xmpp.chat;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import ru.netdedicated.message.MessageService;
import ru.netdedicated.request.ChatRequest;
import ru.netdedicated.request.RequestService;

/**
 * Created by artemz on 03.01.17.
 */
public class XmppChatMessageListener implements ChatMessageListener {
    private final MessageService messageService;
    private final RequestService requestService;
    private final String connId;

    public XmppChatMessageListener(String connId, MessageService messageService, RequestService requestService) {
        this.messageService = messageService;
        this.requestService = requestService;
        this.connId = connId;
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        ChatRequest request = requestService.findByIdent(connId);
        messageService.create(message.getBody(), request);
    }
}
