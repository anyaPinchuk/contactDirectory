package controllers;

import commands.FrontCommand;
import commands.UnknownCommand;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.BirthdayJob;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/app/*")
public class FrontControllerServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(FrontControllerServlet.class);
    private Scheduler scheduler;

    @Override
    public void init() throws ServletException {
        LOG.info("servlet initialize starting");
        JobDetail job = JobBuilder.newJob(BirthdayJob.class)
                .withIdentity("birthdayJob", "group1").build();
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("dummyTriggerName", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 10 * * ?")).build();
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            LOG.error("error while executing schedule's job");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        FrontCommand command = getCommand(request);
        command.initialize(getServletContext(), request, response);
        command.processGet();
    }

    private FrontCommand getCommand(HttpServletRequest request) {
        LOG.info("handle request from user at URL " + request.getRequestURI() + " starting ");
        try {
            request.setCharacterEncoding("UTF-8");
            String command = request.getRequestURI().substring(5);
            Class type = Class.forName(String.format(
                    "commands.%sCommand", command.substring(0, 1).toUpperCase() + command.substring(1)));
            FrontCommand frontCommand = (FrontCommand) type.asSubclass(FrontCommand.class).newInstance();
            return frontCommand;
        } catch (Exception e) {
            LOG.error("such URL doesn't exist, redirect to UnknownCommand", e);
            return new UnknownCommand();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        FrontCommand command = getCommand(request);
        command.initialize(getServletContext(), request, response);
        command.processPost();
    }

    @Override
    public void destroy() {
        LOG.info("destroying scheduler starting");
        if (scheduler != null){
            try {
                scheduler.shutdown(true);
            } catch (SchedulerException e) {
               LOG.error("scheduler was not shutdown");
            }
        }
    }
}
