package ru.netdedicated.jobs;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.netdedicated.xmpp.pool.XmppChatPool;
import ru.netdedicated.xmpp.pool.XmppConnectionPool;

/**
 * One time task created on new incoming support requests
 */
public class XmppChatJob implements Job {
    private Logger logger = LoggerFactory.getLogger(XmppChatJob.class);
    public XmppChatJob() {
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String connId = jobExecutionContext.getMergedJobDataMap().getString("connId");
        if (connId == null){
            throw new JobExecutionException("Required job option \"connId\" is not specified");
        }
        /**
         * TODO:
         * Select XmppAccount based on operator availability
         */
        logger.debug("Creating new XMPP connection for session " + connId);
        try {
            XmppConnectionPool.getInstance().addConnection(connId, XMPPTCPConnectionConfiguration.builder()
                            .setUsernameAndPassword(null,null)
                            .build()
            );
        } catch (Exception e){
            logger.error("XMPP connection error", e);
            return;
        }
        logger.debug("Creating new XMPP chat with operator for session " + connId);
        try {
            XmppChatPool.getInstance().addChat(connId, with)
        }

    }
}
