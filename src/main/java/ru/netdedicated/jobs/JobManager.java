package ru.netdedicated.jobs;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by artemz on 17.12.16.
 */
public class JobManager {

    private Scheduler scheduler;

    public JobManager(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void startNewXmppChat(String connId) throws SchedulerException{
        JobDetail job = newJob(XmppChatJob.class)
                .withIdentity("conn#" + connId)
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("xmppChatTrigger#" + connId)

                .withSchedule(simpleSchedule().withRepeatCount(0))
                .build();

        scheduler.scheduleJob(job, trigger);
    }
}
