import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import utilities.BirthdayJob;
import utilities.FileUploadDocuments;
import utilities.MailSender;

public class MainTest {

//    @Test
//    public void testLoadProperties() throws SchedulerException {
//        JobDetail job = JobBuilder.newJob(BirthdayJob.class)
//                .withIdentity("birthdayJob", "group1").build();
//        Trigger trigger = TriggerBuilder
//                .newTrigger()
//                .withIdentity("dummyTriggerName", "group1")
//                .withSchedule(
//                        CronScheduleBuilder.cronSchedule("0 0 9 * * ?"))
//                .build();
//
//        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//        scheduler.start();
//        scheduler.scheduleJob(job, trigger);

//    }

}
