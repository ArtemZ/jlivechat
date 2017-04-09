package ru.netdedicated.jobs;

import lombok.SneakyThrows;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.netdedicated.client.Client;
import ru.netdedicated.duty.Duty;
import ru.netdedicated.duty.DutyService;
import ru.netdedicated.i18n.I18nService;
import ru.netdedicated.message.MessageService;
import ru.netdedicated.operator.Operator;
import ru.netdedicated.operator.OperatorService;
import ru.netdedicated.request.ChatRequest;
import ru.netdedicated.request.RequestService;
import ru.netdedicated.xmpp.account.XmppAccount;
import ru.netdedicated.xmpp.account.XmppAccountService;
import ru.netdedicated.xmpp.chat.XmppChatMessageListener;
import ru.netdedicated.xmpp.pool.XmppChatPool;
import ru.netdedicated.xmpp.pool.XmppConnectionPool;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * One time task created on new incoming support requests
 */
@DisallowConcurrentExecution
public class XmppChatJob implements Job {
    private Logger logger = LoggerFactory.getLogger(XmppChatJob.class);
    public XmppChatJob() {
    }

    @Override
    @SneakyThrows
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException{
        String connId = jobExecutionContext.getMergedJobDataMap().getString("connId");
        if (connId == null){
            throw new JobExecutionException("Required job option \"connId\" is not specified");
        }

        DutyService dutyService = (DutyService)jobExecutionContext.getScheduler().getContext().get("dutyService");
        XmppAccountService accountService = (XmppAccountService) jobExecutionContext.getScheduler().getContext().get("xmppAccountService");
        MessageService messageService = (MessageService) jobExecutionContext.getScheduler().getContext().get("messageService");
        RequestService requestService = (RequestService) jobExecutionContext.getScheduler().getContext().get("requestService");
        OperatorService operatorService = (OperatorService) jobExecutionContext.getScheduler().getContext().get("operatorService");


        if (dutyService == null){
            throw new JobExecutionException("Required job option \"dutyService\" is not specified" );
        }
        if (accountService == null){
            throw new JobExecutionException("Required job option \"xmppAccountService\" is not specified");
        }
        if (requestService == null){
            throw new JobExecutionException("Required job option \"requestService\" is not specified");
        }
        logger.debug("Finding current duty");
        Duty duty = dutyService.findCurrentDuty(new Date());
        if (duty == null) {
            throw new JobExecutionException("No duties available for now");
        }
        //selecting first available operator in this duty
        List<Operator> operators = duty.getOperators();
        if (operators == null || operators.size() == 0){
            throw new JobExecutionException("No operators assigned to duty " + duty.getId());
        }
        ChatRequest request = requestService.findByIdent(connId);
        Client client = request.getClient();
        I18nService i18nService = new I18nService(new Locale("en"));
        logger.debug("Creating new XMPP connection for session " + connId);

        XmppAccount account = accountService.selectAvailable();
        if (account == null){
            messageService.createStatus(i18nService.getStringForCode("no.operators.available", null), request);
            requestService.close(request);
            return;
        }
        assert XmppConnectionPool.getInstance() != null;
        assert account.getUsername() != null;
        assert account.getPassword() != null;
        assert account.getHost() != null;
        assert account.getPort() != null;
        XMPPTCPConnection connection = null;
        try {
            connection = XmppConnectionPool.getInstance().addConnection(connId, XMPPTCPConnectionConfiguration.builder()
                            .setUsernameAndPassword(account.getUsername(), account.getPassword())
                            .setHost(account.getHost())
                            .setPort(account.getPort())
                            .setServiceName(account.getHost())
                            .setHostnameVerifier(((s, sslSession) -> true))
                            .build()
            );

        } catch (Exception e){
            logger.error("XMPP connection error", e);
            return;
        } finally {
            if (connection != null){
                System.out.println("returning connection " + connId + " to the pool");
                XmppConnectionPool.getInstance().returnObject(connId, connection);
                System.out.println("numactive after return: " + XmppConnectionPool.getInstance().getNumActive(connId));
            }
        }
        logger.debug("Creating new XMPP chat with operator for session " + connId);
        Chat newChat;
        try {
            newChat = XmppChatPool.getInstance().addChat(
                    connId,
                    operators.get(0).getJabber(),
                    new XmppChatMessageListener(connId, messageService, requestService, operatorService, i18nService)
            );
        } catch (Exception e){
            throw new JobExecutionException("Unable to create chat with " + operators.get(0).getJabber(), e);
        }
        try {
            newChat.sendMessage(i18nService.getStringForCode("operator.new.support.request", new String[]{client.getName(), client.getEmail(), connId}));
        } catch (SmackException.NotConnectedException e){
            logger.error("XMPP Connection error: " + e.getMessage(), e);
        } finally {
            XmppChatPool.getInstance().returnObject(connId, newChat);
        }


    }
}
