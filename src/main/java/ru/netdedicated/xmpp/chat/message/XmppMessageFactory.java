package ru.netdedicated.xmpp.chat.message;

import org.jivesoftware.smack.packet.Message;
import org.jxmpp.util.XmppStringUtils;
import ru.netdedicated.operator.Operator;
import ru.netdedicated.operator.OperatorService;
import ru.netdedicated.request.ChatRequest;
import ru.netdedicated.request.RequestService;
import ru.netdedicated.xmpp.chat.command.CloseCommand;
import ru.netdedicated.xmpp.chat.command.Command;
import ru.netdedicated.xmpp.chat.command.HelloCommand;

/**
 * Created by artemz on 02.04.17.
 */
public class XmppMessageFactory {
    private final Message smackMessage;
    private final String connId;
    private final RequestService requestService;
    private final OperatorService operatorService;
    public XmppMessageFactory(Message smackMessage, String connId, RequestService requestService, OperatorService operatorService) {
        this.smackMessage = smackMessage;
        this.connId = connId;
        this.requestService = requestService;
        this.operatorService = operatorService;
    }
    public IncomingMessage getMessage(){
        final String from = XmppStringUtils.parseBareJid(smackMessage.getFrom());
        final Operator senderOp = operatorService.getByJid(from);
        if (senderOp == null){
            return new UnauthorizedMessage() {
                @Override
                public String getReason() {
                    return "No operators with jid " + from;
                }

                @Override
                public String getSenderJid() {
                    return from;
                }
            };
        }
        final String body = smackMessage.getBody().trim();
        final boolean isClose = body.equals("!close");
        final boolean isHello = body.equals("!hello");
        ChatRequest request = requestService.findByIdent(connId);
        Operator op = request.getOp();

        if ((isClose && op == null) || (isClose && !op.getJabber().equals(from))){
            return new UnauthorizedMessage() {
                @Override
                public String getReason() {
                    return "You are not assigned to this support request, closing is not allowed";
                }

                @Override
                public String getSenderJid() {
                    return from;
                }
            };
        } else if (isClose && op.getJabber().equals(from)){

            return new CommandMessage() {
                @Override
                public Command getCommand() {
                    return new CloseCommand();
                }

                @Override
                public Operator getOperator() {
                    return op;
                }

                @Override
                public String getSenderJid() {
                    return from;
                }
            };
        }

        if (isHello && op != null){
            return new UnauthorizedMessage() {
                @Override
                public String getReason() {
                    return "This support request is already assigned to " + op.getName();
                }

                @Override
                public String getSenderJid() {
                    return from;
                }
            };
        } else if (isHello){
            return new CommandMessage() {
                @Override
                public Command getCommand() {
                    return new HelloCommand();
                }

                @Override
                public Operator getOperator() {
                    return senderOp;
                }

                @Override
                public String getSenderJid() {
                    return from;
                }
            };
        }
        return new TextMessage(op,from,smackMessage.getBody());
    }
}
