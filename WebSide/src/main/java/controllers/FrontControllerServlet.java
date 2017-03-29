package controllers;

import commands.FrontCommand;
import commands.UnknownCommand;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import utilities.BirthdayJob;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/app/*")
public class FrontControllerServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(FrontControllerServlet.class);


    @Override
    public void init() throws ServletException {
        JobDetail job = JobBuilder.newJob(BirthdayJob.class)
                .withIdentity("birthdayJob", "group1").build();
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("dummyTriggerName", "group1")
                .withSchedule(
                        CronScheduleBuilder.cronSchedule("0 0 9 * * ?"))
                .build();
        Scheduler scheduler;
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            LOG.error(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        FrontCommand command = getCommand(request);
        command.init(getServletContext(), request, response);
        command.processGet();

    }

    private FrontCommand getCommand(HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
            String command = request.getRequestURI().substring(5);
            LOG.info("handle request from user at URL " + command + " starting ");
            Class type = Class.forName(String.format(
                    "commands.%sCommand", command.substring(0, 1).toUpperCase() + command.substring(1)));
            return (FrontCommand) type.asSubclass(FrontCommand.class).newInstance();
        } catch (Exception e) {
            LOG.error("such URL doesn't exist, redirect to UnknownCommand", e);
            return new UnknownCommand();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        FrontCommand command = getCommand(request);
        command.init(getServletContext(), request, response);
        command.processPost();
    }

}
